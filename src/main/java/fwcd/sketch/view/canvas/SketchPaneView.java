package fwcd.sketch.view.canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import fwcd.fructose.geometry.Direction;
import fwcd.fructose.swing.SelectedButtonPanel;
import fwcd.fructose.swing.View;
import fwcd.sketch.model.SketchBoardModel;
import fwcd.sketch.view.tools.CommonSketchTool;
import fwcd.sketch.view.tools.SketchTool;
import fwcd.sketch.view.utils.ColorButton;
import fwcd.sketch.view.utils.ListenableRenderable;

public class SketchPaneView implements View {
	private final JPanel component;
	
	private final SketchBoardView board;
	private final JToolBar toolBar;
	
	private SketchPaneView(SketchBoardModel model, Direction toolBarLocation, boolean foldMenus, Color... colors) {
		component = new JPanel();
		component.setLayout(new BorderLayout());
		
		board = new SketchBoardView(model);
		component.add(board.getComponent(), BorderLayout.CENTER);
		
		boolean horizontal = toolBarLocation == Direction.UP || toolBarLocation == Direction.DOWN;
		toolBar = new JToolBar(horizontal ? JToolBar.HORIZONTAL : JToolBar.VERTICAL);
		toolBar.setPreferredSize(new Dimension(40, 40));
		toolBar.setFloatable(false);
		
		SelectedButtonPanel toolsPane = new SelectedButtonPanel(horizontal, Color.GRAY);
		toolsPane.setFolding(foldMenus);
		for (CommonSketchTool enumTool : CommonSketchTool.values()) {
			SketchTool tool = enumTool.get();
			toolsPane.add(new JButton(tool.getIcon()), () -> board.selectTool(tool));
		}
		toolBar.add(toolsPane.getComponent());

		toolBar.add(newSpacer());
		
		SelectedButtonPanel colorPane = new SelectedButtonPanel(horizontal, Color.GRAY);
		colorPane.setFolding(foldMenus);
		for (Color color : colors) {
			colorPane.add(new ColorButton(color), () -> board.getBrushProperties().setColor(color));
		}
		toolBar.add(colorPane.getComponent());
		
		component.add(toolBar, toBorderLayoutPosition(toolBarLocation));
	}
	
	public SketchBoardView getBoard() { return board; }
	
	private Component newSpacer() {
		return Box.createRigidArea(new Dimension(10, 10));
	}

	private Object toBorderLayoutPosition(Direction toolBarPos) {
		switch (toolBarPos) {
			case DOWN: return BorderLayout.SOUTH;
			case LEFT: return BorderLayout.WEST;
			case RIGHT: return BorderLayout.EAST;
			case UP: return BorderLayout.NORTH;
			default: throw new IllegalArgumentException("Invalid direction");
		}
	}

	@Override
	public JComponent getComponent() {
		return component;
	}
	
	public static class Builder {
		private final SketchBoardModel model;
		private Direction toolBarLocation = Direction.LEFT;
		private ListenableRenderable[] overlays = {};
		private boolean foldMenus = false;
		private Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};
		
		public Builder(SketchBoardModel model) {
			this.model = model;
		}
		
		public Builder toolBarLocation(Direction toolBarLocation) {
			this.toolBarLocation = toolBarLocation;
			return this;
		}
		
		public Builder foldMenus(boolean foldMenus) {
			this.foldMenus = foldMenus;
			return this;
		}
		
		public Builder overlays(ListenableRenderable... overlays) {
			this.overlays = overlays;
			return this;
		}
		
		public Builder colors(Color... colors) {
			this.colors = colors;
			return this;
		}
		
		public SketchPaneView build() {
			SketchPaneView pane = new SketchPaneView(model, toolBarLocation, foldMenus, colors);
			for (ListenableRenderable overlay : overlays) {
				pane.getBoard().pushOverlay(overlay);
			}
			return pane;
		}
	}
}
