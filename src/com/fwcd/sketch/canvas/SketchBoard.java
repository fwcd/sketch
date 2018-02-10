package com.fwcd.sketch.canvas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fwcd.fructose.EventListenerList;
import com.fwcd.fructose.ListenerList;
import com.fwcd.fructose.Pair;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.fructose.swing.RenderPanel;
import com.fwcd.fructose.swing.Rendereable;
import com.fwcd.fructose.swing.Viewable;
import com.fwcd.fructose.swing.properties.BoolProperty;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.SketchBoardModel;
import com.fwcd.sketch.model.SketchItem;
import com.fwcd.sketch.tools.EnumSketchTool;
import com.fwcd.sketch.tools.Eraser;
import com.fwcd.sketch.tools.SketchTool;
import com.fwcd.sketch.utils.PolymorphicSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class SketchBoard implements Viewable, Rendereable, Iterable<SketchItem> {
	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(SketchItem.class, new PolymorphicSerializer<SketchItem>())
			.create();
	
	private final JPanel view;
	private SketchBoardModel model = new SketchBoardModel();
	
	private int snapSensivity = 10;
	private int gridConstant = 30;
	private BoolProperty showGrid = new BoolProperty(false);
	private BoolProperty snapToGrid = new BoolProperty(false);
	
	private SketchTool tool = EnumSketchTool.BRUSH.get();
	private BrushProperties brushProps = new BrushProperties();
	
	private ListenerList changeListeners = new ListenerList();
	private EventListenerList<SketchItem> addListeners = new EventListenerList<>();
	private EventListenerList<SketchItem> removeListeners = new EventListenerList<>();
	private ListenerList clearListeners = new ListenerList();

	private Set<Rendereable> hiddenItems = new HashSet<>();
	
	public SketchBoard() {
		view = new RenderPanel(this);
		view.setOpaque(false);
		view.setFocusable(true);
		
		showGrid.addChangeListener(() -> view.repaint());
		
		MouseAdapter mouseAdapter = new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				tool.onMouseDown(snappedPos(e), SketchBoard.this);
				view.repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				tool.onMouseUp(snappedPos(e), SketchBoard.this);
				view.repaint();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				tool.onMouseDrag(snappedPos(e), SketchBoard.this);
				view.repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				view.setCursor(tool.getCursor());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				view.requestFocusInWindow();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() <= 1) {
					tool.onMouseClick(snappedPos(e), SketchBoard.this);
				} else {
					tool.onMouseDoubleClick(snappedPos(e), SketchBoard.this);
				}
			}
			
		};
		
		view.addMouseListener(mouseAdapter);
		view.addMouseMotionListener(mouseAdapter);
		
		KeyAdapter keyAdapter = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				tool.onKeyPress(e, SketchBoard.this);
			}
			
		};
		
		view.addKeyListener(keyAdapter);
		
		brushProps.getThicknessProperty().addChangeListener(this::repaint);
	}

	private Vector2D snappedPos(MouseEvent e) {
		if (snapToGrid.get()) {
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

	public void load(File file) {
		try {
			model = gson.fromJson(new FileReader(file), SketchBoardModel.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			JOptionPane.showMessageDialog(view, "A " + e.getClass().getSimpleName() + " occurred while loading your file");
		}
		
		view.repaint();
	}
	
	public void save(File file) {
		try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
			writer.print(gson.toJson(model));
		} catch (JsonIOException | IOException e) {
			JOptionPane.showMessageDialog(view, "A " + e.getClass().getSimpleName() + " occurred while saving your file.");
		}
	}
	
	public BoolProperty getShowGrid() {
		return showGrid;
	}
	
	public void add(SketchItem item) {
		model.add(item);
		view.repaint();
		
		addListeners.fire(item);
		changeListeners.fire();
	}
	
	public void remove(SketchItem item) {
		model.remove(item);
		view.repaint();
		
		removeListeners.fire(item);
		changeListeners.fire();
	}
	
	public void modify(SketchItem item, SketchItem replacement) {
		model.replace(item, replacement);
		view.repaint();
		
		removeListeners.fire(item);
		addListeners.fire(replacement);
		changeListeners.fire();
	}
	
	public void clear() {
		model.clear();
		view.repaint();
		
		clearListeners.fire();
		changeListeners.fire();
	}

	public void show(SketchItem item) {
		hiddenItems.remove(item);
		changeListeners.fire();
	}

	public void hide(SketchItem item) {
		hiddenItems.add(item);
		changeListeners.fire();
	}
	
	@Override
	public JPanel getView() {
		return view;
	}

	public BrushProperties getBrushProperties() {
		return brushProps;
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension canvasSize) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		renderModel(g2d, canvasSize);
		tool.render(g2d, canvasSize, this); // Perform tool-specific rendering
	}
	
	public void renderTo(Graphics2D g2d) {
		renderModel(g2d, view.getSize());
	}

	private void renderModel(Graphics2D g2d, Dimension canvasSize) {
		g2d.setColor(model.getBackground());
		g2d.fillRect(0, 0, (int) canvasSize.getWidth(), (int) canvasSize.getHeight());
		
		if (showGrid.get()) {
			g2d.setColor(Color.DARK_GRAY);
			
			for (int x=gridConstant; x<canvasSize.getWidth(); x+=gridConstant) {
				for (int y=gridConstant; y<canvasSize.getHeight(); y+=gridConstant) {
					g2d.fillOval(x - 1, y - 1, 2, 2);
				}
			}
		}
		
		for (Rendereable item : model.getItems()) {
			if (!hiddenItems.contains(item)) {
				item.render(g2d, canvasSize);
			}
		}
	}

	@Override
	public Iterator<SketchItem> iterator() {
		return model.getItems().iterator();
	}

	public BoolProperty getSnapToGrid() {
		return snapToGrid;
	}

	public void setModel(SketchBoardModel model) {
		this.model = model;
	}

	public void addChangeListener(Runnable listener) {
		changeListeners.add(listener);
	}

	public void selectTool(SketchTool tool) {
		this.tool = tool;
	}

	public SketchTool getSelectedTool() {
		return tool;
	}

	public Color getBackground() {
		return model.getBackground();
	}
	
	public void setBackground(Color color) {
		model.setBackground(color);
	}

	public void repaint() {
		view.repaint();
	}

	/**
	 * Fetches the items on this sketch board. <b>Generally, it is
	 * preferrable to iterate directly over this object as that
	 * will achieve the same result.</b>
	 */
	public Iterable<SketchItem> getItems() {
		return model.getItems();
	}
	
	/**
	 * Fetches all items in it's decomposed state. <b>In most cases,
	 * you will have no use of this method - iterate directly instead. An exception
	 * of this rule is the {@link Eraser}.</b>
	 * 
	 * @return Returns an iterable of pairs - A is always the item and B is a decomposed sub-item or the same
	 */
	public Iterable<Pair<SketchItem, SketchItem>> getDecomposedItems() {
		return model.getDecomposedItems();
	}
}
