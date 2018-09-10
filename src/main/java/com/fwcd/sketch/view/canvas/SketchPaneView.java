package com.fwcd.sketch.view.canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.fwcd.fructose.geometry.Direction;
import com.fwcd.fructose.swing.SelectedButtonPanel;
import com.fwcd.fructose.swing.View;
import com.fwcd.sketch.model.SketchBoardModel;
import com.fwcd.sketch.view.tools.CommonSketchTool;
import com.fwcd.sketch.view.tools.SketchTool;
import com.fwcd.sketch.utils.ColorButton;

public class SketchPaneView implements View {
	private final JPanel component;
	
	private final SketchBoardView board;
	private final JToolBar toolBar;
	
	public SketchPaneView(SketchBoardModel model) {
		this(model, Direction.LEFT, false, Color.BLACK, Color.RED, Color.YELLOW, Color.BLUE);
	}
	
	public SketchPaneView(SketchBoardModel model, Direction toolBarPos, boolean folding, Color... colors) {
		component = new JPanel();
		component.setLayout(new BorderLayout());
		
		board = new SketchBoardView(model);
		component.add(board.getComponent(), BorderLayout.CENTER);
		
		boolean horizontal = toolBarPos == Direction.UP || toolBarPos == Direction.DOWN;
		toolBar = new JToolBar(horizontal ? JToolBar.HORIZONTAL : JToolBar.VERTICAL);
		toolBar.setPreferredSize(new Dimension(40, 40));
		toolBar.setFloatable(false);
		
		SelectedButtonPanel toolsPane = new SelectedButtonPanel(horizontal, Color.GRAY);
		toolsPane.setFolding(folding);
		for (CommonSketchTool enumTool : CommonSketchTool.values()) {
			SketchTool tool = enumTool.get();
			toolsPane.add(new JButton(tool.getIcon()), () -> board.selectTool(tool));
		}
		toolBar.add(toolsPane.getComponent());

		toolBar.add(newSpacer());
		
		SelectedButtonPanel colorPane = new SelectedButtonPanel(horizontal, Color.GRAY);
		colorPane.setFolding(folding);
		for (Color color : colors) {
			colorPane.add(new ColorButton(color), () -> board.getBrushProperties().setColor(color));
		}
		toolBar.add(colorPane.getComponent());
		
		component.add(toolBar, toBorderLayoutPosition(toolBarPos));
	}
	
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
}
