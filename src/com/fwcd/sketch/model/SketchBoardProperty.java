package com.fwcd.sketch.model;

import java.util.function.Consumer;

import com.fwcd.fructose.properties.BasicProperty;
import com.fwcd.sketch.canvas.SketchBoard;
import com.fwcd.sketch.canvas.SketchPane;

public class SketchBoardProperty extends BasicProperty<SketchBoardModel, SketchBoard> {
	private SketchBoard board;
	
	public SketchBoardProperty() {
		set(new SketchBoardModel());
	}
	
	public void bindObserver(Consumer<SketchBoard> onChange) {
		bindObserver(onChange, onChange);
	}
	
	public void bindObserver(Consumer<SketchBoard> onBind, Consumer<SketchBoard> onChange) {
		addChangeListener(() -> {
			if (board != null) {
				onChange.accept(board);
			}
		});
		announceChange();
	}
	
	public void bind(SketchPane component) {
		component.setModel(get());
		component.addChangeListener(() -> announceChange());
		announceChange();
	}
	
	@Override
	public void bind(SketchBoard board) {
		this.board = board;
		
		board.setModel(get());
		board.addChangeListener(() -> announceChange());
		announceChange();
	}
}
