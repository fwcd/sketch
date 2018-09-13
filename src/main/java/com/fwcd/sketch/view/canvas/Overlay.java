package com.fwcd.sketch.view.canvas;

import java.awt.Dimension;
import java.awt.Graphics2D;

import com.fwcd.fructose.swing.Renderable;
import com.fwcd.sketch.view.utils.ListenableRenderable;

final class Overlay implements Renderable {
	private final ListenableRenderable renderable;
	private final Runnable listener;
	
	public Overlay(ListenableRenderable renderable, Runnable listener) {
		this.renderable = renderable;
		this.listener = listener;
		
		renderable.listenForChanges(listener);
	}
	
	public void dispose() {
		renderable.unlistenForChanges(listener);
	}
	
	@Override
	public void render(Graphics2D g2d, Dimension size) {
		renderable.render(g2d, size);
	}
}
