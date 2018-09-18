# Sketch
2D vector graphics drawing widget for Swing.

![Screenshot](screenshot.png)

## Getting Started
Adding a Sketch widget to an existing Swing application is simple:

```java
SketchBoardModel model = new SketchBoardModel();
SketchPaneView sketchPane = new SketchPaneView.Builder(model)
	.colors(Color.BLACK, Color.MAGENTA, Color.YELLOW)
	.foldMenus(false)
	.build();
topLevelPanel.add(sketchPane.getComponent(), BorderLayout.CENTER);
```
