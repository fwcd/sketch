package com.fwcd.sketch.view.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.view.canvas.SketchBoardView;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.ColoredText;

public class TextEditingTool extends EditingTool<ColoredText> {
	private Vector2D pos;
	private String text = "";
	
	@Override
	public void edit(ColoredText coloredText) {
		pos = coloredText.getPos();
		text = coloredText.getTotalText();
	}
	
	@Override
	public ColoredText get(SketchBoardView board) {
		BrushProperties properties = board.getBrushProperties();
		ColoredText coloredText = new ColoredText(
				text,
				properties.getColor(),
				properties.getThicknessProperty().getValue(),
				pos
		);
		return coloredText;
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
			
			ColoredText coloredText = get(board);
			coloredText.render(g2d, canvasSize);
			
			FontMetrics metrics = g2d.getFontMetrics();
			Vector2D cursorPos = coloredText
					.getLastLinePos()
					.add(new Vector2D(metrics.stringWidth(coloredText.lastLine()), metrics.getHeight()));
			
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(
					(int) cursorPos.getX(),
					(int) cursorPos.getY() - (metrics.getHeight() * 2),
					metrics.getHeight() / 10,
					metrics.getHeight()
			);
		}
	}

	private boolean isValidKey(char c) {
		return Character.isDefined(c);
	}
	
	@Override
	public void onKeyPress(KeyEvent e, SketchBoardView sketchBoard) {
		if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if (text.length() > 0) {
				text = text.substring(0, text.length() - 1);
			}
		} else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			text += "\n";
		} else if (pos != null && isValidKey(e.getKeyChar())) {
			text += e.getKeyChar();
		}
		
		sketchBoard.repaint();
	}

	public void clear() {
		pos = null;
		text = "";
	}
}
