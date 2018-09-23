package com.fwcd.sketch.view.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.structs.ArrayStack;
import com.fwcd.fructose.structs.Stack;
import com.fwcd.fructose.swing.MouseHandler;
import com.fwcd.fructose.swing.RenderPanel;
import com.fwcd.fructose.swing.Renderable;
import com.fwcd.fructose.swing.View;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.SketchBoardModel;
import com.fwcd.sketch.model.items.BoardItem;
import com.fwcd.sketch.model.items.SketchItemVisitor;
import com.fwcd.sketch.view.tools.CommonSketchTool;
import com.fwcd.sketch.view.tools.SketchTool;
import com.fwcd.sketch.view.utils.ListenableRenderable;

public class SketchBoardView implements View {
	private final JPanel component;
	private final SketchBoardModel model;
	
	private int snapSensivity = 10;
	private int gridConstant = 30;
	
	private SketchTool tool = CommonSketchTool.BRUSH.get();
	private BrushProperties brushProps = new BrushProperties();
	
	private Stack<Overlay> overlays = new ArrayStack<>();
	
	public SketchBoardView(SketchBoardModel model) {
		this.model = model;
		
		component = new RenderPanel(this::render);
		component.setOpaque(false);
		component.setFocusable(true);
		
		model.getShowGrid().listen(s -> component.repaint());
		model.getItemEventBus().getChangeListeners().add(s -> component.repaint());
		
		setupInputListeners();
		
		brushProps.getThicknessProperty().addChangeListener(this::repaint);
	}
	
	private void setupInputListeners() {
		MouseHandler mouseHandler = new MouseHandler() {
			@Override
			public void mousePressed(MouseEvent e) {
				tool.onMouseDown(snappedPos(e), SketchBoardView.this);
				component.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				tool.onMouseUp(snappedPos(e), SketchBoardView.this);
				component.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				tool.onMouseDrag(snappedPos(e), SketchBoardView.this);
				component.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				component.setCursor(tool.getCursor());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				component.requestFocusInWindow();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() <= 1) {
					tool.onMouseClick(snappedPos(e), SketchBoardView.this);
				} else {
					tool.onMouseDoubleClick(snappedPos(e), SketchBoardView.this);
				}
			}
		};
		
		KeyAdapter keyAdapter = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				tool.onKeyPress(e, SketchBoardView.this);
			}
		};
		
		mouseHandler.connect(component);
		component.addKeyListener(keyAdapter);
	}
	
	private Vector2D snappedPos(MouseEvent e) {
		if (model.getSnapToGrid().get()) {
			return new Vector2D(snapCoordinate(e.getX()), snapCoordinate(e.getY()));
		} else {
			return getPos(e);
		}
	}

	private Vector2D getPos(MouseEvent e) {
		return new Vector2D(e.getX(), e.getY());
	}
	
	private int snapCoordinate(int c) {
		int mod = c % gridConstant;
		
		if (mod < snapSensivity) {
			return c - mod;
		} else if (mod > (gridConstant - snapSensivity)) {
			return c - mod + gridConstant;
		} else {
			return c;
		}
	}
	
	@Override
	public JPanel getComponent() {
		return component;
	}

	public BrushProperties getBrushProperties() {
		return brushProps;
	}
	
	public SketchBoardModel getModel() {
		return model;
	}
	
	private void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		renderModel(g2d, canvasSize);
		tool.render(g2d, canvasSize, this); // Perform tool-specific rendering
		
		for (Renderable overlay : overlays) {
			overlay.render(g2d, canvasSize);
		}
	}
	
	public void renderTo(Graphics2D g2d) {
		renderModel(g2d, component.getSize());
	}

	private void renderModel(Graphics2D g2d, Dimension canvasSize) {
		g2d.setColor(model.getBackground().get());
		g2d.fillRect(0, 0, (int) canvasSize.getWidth(), (int) canvasSize.getHeight());
		
		if (model.getShowGrid().get()) {
			g2d.setColor(Color.DARK_GRAY);
			
			for (int x=gridConstant; x<canvasSize.getWidth(); x+=gridConstant) {
				for (int y=gridConstant; y<canvasSize.getHeight(); y+=gridConstant) {
					g2d.fillOval(x - 1, y - 1, 2, 2);
				}
			}
		}
		
		SketchItemVisitor renderer = new ItemRenderer(g2d);
		for (BoardItem item : model.getItems()) {
			item.accept(renderer);
		}
	}
	
	public void pushOverlay(ListenableRenderable renderable) {
		overlays.push(new Overlay(renderable, component::repaint));
		component.repaint();
	}
	
	public void popOverlay() {
		Overlay overlay = overlays.pop();
		overlay.dispose();
	}
	
	public void selectTool(SketchTool tool) {
		this.tool = tool;
	}

	public SketchTool getSelectedTool() {
		return tool;
	}

	public void repaint() {
		component.repaint();
	}
}
