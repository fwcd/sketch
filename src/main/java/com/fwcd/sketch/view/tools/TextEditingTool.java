package com.fwcd.sketch.view.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import com.fwcd.fructose.Option;
import com.fwcd.fructose.geometry.Vector2D;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.items.ColoredText;
import com.fwcd.sketch.view.canvas.SketchBoardView;
import com.fwcd.sketch.view.model.TextEditingToolModel;
import com.fwcd.sketch.view.model.TextPosition;

public class TextEditingTool extends EditingTool<ColoredText> {
	private final Map<Predicate<KeyEvent>, Runnable> keybinds = new HashMap<>();
	private TextEditingToolModel text = new TextEditingToolModel();
	private Option<Vector2D> pos = Option.empty();
	
	public TextEditingTool() {
		registerKeybinds();
	}
	
	@Override
	public void edit(ColoredText coloredText) {
		pos = Option.of(coloredText.getPos());
		text = new TextEditingToolModel(coloredText.getLines());
	}
	
	@Override
	public ColoredText getItem(SketchBoardView board) {
		BrushProperties properties = board.getBrushProperties();
		return new ColoredText(
			text.toLines(),
			properties.getColor(),
			properties.getThicknessProperty().getValue(),
			pos.unwrap()
		);
	}
	
	@Override
	public void onMouseDown(Vector2D pos, SketchBoardView board) {
		this.pos = Option.of(pos);
	}

	@Override
	public void onMouseDrag(Vector2D pos, SketchBoardView board) {
		this.pos = Option.of(pos);
	}

	@Override
	public void onMouseUp(Vector2D pos, SketchBoardView board) {
		board.getComponent().repaint();
	}

	@Override
	public void render(Graphics2D g2d, Dimension canvasSize, SketchBoardView board) {
		if (pos != null) {
			int xPos = (int) pos.unwrap().getX();
			int yPos = (int) pos.unwrap().getY();
			
			g2d.setColor(Color.BLACK);
			g2d.fillRect(xPos - 5, yPos - 5, 10, 10);
			
			TextPosition cursor = text.getCursor();
			
			FontMetrics metrics = g2d.getFontMetrics();
			int lineAscent = metrics.getAscent();
			int lineHeight = metrics.getHeight();
			int cursorWidth = 2;
			int x = xPos + metrics.stringWidth(text.getLineFragment(cursor.getLine(), 0, cursor.getColumn()));
			int y = yPos + (lineHeight * cursor.getLine()) - lineAscent;
			
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(x, y, cursorWidth, lineHeight);
		}
	}

	private boolean isValidKey(char c) {
		return Character.isDefined(c);
	}
	
	private void registerKeybinds() {
		// Note that the text callback intentionally does not use method references in
		// order to capture 'this' instead of the 'text' object itself
		keybinds.put(e -> e.getKeyCode() == KeyEvent.VK_BACK_SPACE, () -> text.backspace());
		keybinds.put(e -> e.getKeyCode() == KeyEvent.VK_ENTER, () -> text.enter());
		keybinds.put(e -> e.getKeyCode() == KeyEvent.VK_LEFT, () -> text.moveCursorLeft());
		keybinds.put(e -> e.getKeyCode() == KeyEvent.VK_RIGHT, () -> text.moveCursorRight());
		keybinds.put(e -> e.getKeyCode() == KeyEvent.VK_UP, () -> text.moveCursorUp());
		keybinds.put(e -> e.getKeyCode() == KeyEvent.VK_DOWN, () -> text.moveCursorDown());
		
		// TODO: Copy & paste
	}
	
	@Override
	public void onKeyPress(KeyEvent e, SketchBoardView sketchBoard) {
		boolean handled = false;
		
		for (Predicate<KeyEvent> predicate : keybinds.keySet()) {
			if (predicate.test(e)) {
				keybinds.get(predicate).run();
				handled |= true;
			}
		}
		
		if (!handled && pos.isPresent() && isValidKey(e.getKeyChar())) {
			text.insert(e.getKeyChar());
		}
		
		sketchBoard.repaint();
	}

	public void clear() {
		pos = null;
		text = new TextEditingToolModel();
	}
}
