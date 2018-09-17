package com.fwcd.sketch.view.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.items.ColoredText;
import com.fwcd.sketch.view.canvas.ItemRenderer;
import com.fwcd.sketch.view.canvas.SketchBoardView;
import com.fwcd.sketch.view.model.TextEditingToolModel;
import com.fwcd.sketch.view.model.TextPosition;

public class TextEditingTool extends EditingTool<ColoredText> {
	private Vector2D pos;
	private TextEditingToolModel text = new TextEditingToolModel();
	
	@Override
	public void edit(ColoredText coloredText) {
		pos = coloredText.getPos();
		text = new TextEditingToolModel(coloredText.getLines());
	}
	
	@Override
	public ColoredText getItem(SketchBoardView board) {
		BrushProperties properties = board.getBrushProperties();
		return new ColoredText(
			text.toLines(),
			properties.getColor(),
			properties.getThicknessProperty().getValue(),
			pos
		);
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView board) {
		this.pos = pos;
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView board) {
		this.pos = pos;
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView board) {
		board.getComponent().repaint();
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		if (pos != null) {
			g2d.setColor(Color.BLACK);
			g2d.fillRect((int) pos.getX() - 5, (int) pos.getY() - 5, 10, 10);
			
			TextPosition cursor = text.getCursor();
			
			ColoredText coloredText = getItem(board);
			coloredText.accept(new ItemRenderer(g2d));
			
			FontMetrics metrics = g2d.getFontMetrics();
			int lineAscent = metrics.getAscent();
			int lineHeight = metrics.getHeight();
			int cursorWidth = 2;
			int x = (int) pos.getX() + metrics.stringWidth(text.getLineFragment(cursor.getLine(), 0, cursor.getColumn()));
			int y = (int) pos.getY() + (lineHeight * cursor.getLine()) - lineAscent;
			
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(x, y, cursorWidth, lineAscent);
		}
	}

	private boolean isValidKey(char c) {
		return Character.isDefined(c);
	}
	
	@Override
	public void onKeyPress(KeyEvent e, SketchBoardView sketchBoard) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			text.backspace();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			text.enter();
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			text.moveCursorLeft();
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			text.moveCursorRight();
		} else if (pos != null && isValidKey(e.getKeyChar())) {
			text.insert(e.getKeyChar());
		}
		
		sketchBoard.repaint();
	}

	public void clear() {
		pos = null;
		text = new TextEditingToolModel();
	}
}
