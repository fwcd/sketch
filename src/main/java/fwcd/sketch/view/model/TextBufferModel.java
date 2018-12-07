package fwcd.sketch.view.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextBufferModel {
	private static final String NEWLINE = "\n";
	private final List<StringBuilder> lines;
	
	public TextBufferModel() {
		lines = new ArrayList<>();
		lines.add(new StringBuilder());
	}
	
	public TextBufferModel(String[] lines) {
		this.lines = Arrays.stream(lines)
			.map(StringBuilder::new)
			.collect(Collectors.toCollection(ArrayList::new));
		if (this.lines.isEmpty()) {
			this.lines.add(new StringBuilder());
		}
	}
	
	public void appendLine() {
		lines.add(new StringBuilder());
	}
	
	public StringBuilder getLine(int index) {
		return lines.get(index);
	}
	
	public void insertLine(int index) {
		lines.add(index, new StringBuilder());
	}
	
	public void insertLineBreak(int line, int column) {
		StringBuilder lineBuffer = getLine(line);
		String right = lineBuffer.substring(column);
		lineBuffer.delete(column, lineBuffer.length());
		lines.add(line + 1, new StringBuilder(right));
	}
	
	public void removeLine(int index) {
		if ((lines.size() <= 1) && (index == 0)) {
			lines.clear();
			lines.add(new StringBuilder());
		} else {
			lines.remove(index);
		}
	}
	
	public int lineCount() {
		return lines.size();
	}
	
	public String join() {
		StringBuilder result = new StringBuilder();
		for (StringBuilder line : lines) {
			result.append(line).append(NEWLINE);
		}
		return result.toString();
	}

	public String[] toLines() {
		return lines.stream()
			.map(StringBuilder::toString)
			.toArray(String[]::new);
	}

	public void mergeLines(int baseIndex, int appendedAndDeletedIndex) {
		StringBuilder appended = getLine(appendedAndDeletedIndex);
		removeLine(appendedAndDeletedIndex);
		getLine(baseIndex).append(appended);
	}
}
