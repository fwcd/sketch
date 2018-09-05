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
import com.fwcd.sketch.view.tools.EnumSketchTool;
import com.fwcd.sketch.view.tools.SketchTool;
import com.fwcd.sketch.utils.ColorButton;

public class SketchPaneView implements View {
	private final JPanel view;
	
	private final SketchBoardView board;
	private final JToolBar toolBar;
	
	public SketchPaneView() {
		this(Direction.LEFT, false, Color.BLACK, Color.RED, Color.YELLOW, Color.BLUE);
	}
	
	public SketchPaneView(Direction toolBarPos, boolean folding, Color... colors) {
		view = new JPanel();
		view.setLayout(new BorderLayout());
		
		board = new SketchBoardView(new SketchBoardModel());
		view.add(board.getComponent(), BorderLayout.CENTER);
		
		boolean horizontal = toolBarPos == Direction.UP || toolBarPos == Direction.DOWN;
		toolBar = new JToolBar(horizontal ? JToolBar.HORIZONTAL : JToolBar.VERTICAL);
		toolBar.setPreferredSize(new Dimension(40, 40));
		toolBar.setFloatable(false);
		
		SelectedButtonPanel toolsPane = new SelectedButtonPanel(horizontal, Color.GRAY);
		toolsPane.setFolding(folding);
		for (EnumSketchTool enumTool : EnumSketchTool.values()) {
			SketchTool tool = enumTool.get();
			toolsPane.add(new JButton(tool.getIcon()), () -> board.selectTool(tool));
		}
		toolBar.add(toolsPane.getComponent());

		toolBar.add(spacer());
		
		SelectedButtonPanel colorPane = new SelectedButtonPanel(horizontal, Color.GRAY);
		colorPane.setFolding(folding);
		for (Color color : colors) {
			colorPane.add(new ColorButton(color), () -> board.getBrushProperties().setColor(color));
		}
		toolBar.add(colorPane.getComponent());
		
		view.add(toolBar, getBorderPos(toolBarPos));
	}
	
	private Component spacer() {
		return Box.createRigidArea(new Dimension(10, 10));
	}

	private Object getBorderPos(Direction toolBarPos) {
		switch (toolBarPos) {
		
		case DOWN:
			return BorderLayout.SOUTH;
		case LEFT:
			return BorderLayout.WEST;
		case RIGHT:
			return BorderLayout.EAST;
		case UP:
			return BorderLayout.NORTH;
		default:
			throw new IllegalArgumentException("Invalid direction");
		
		}
	}

	@Override
	public JComponent getComponent() {
		return view;
	}
}
