package com.fwcd.sketch.view.utils;

import com.fwcd.fructose.swing.Renderable;

public interface ListenableRenderable extends Renderable {
	default void listenForChanges(Runnable listener) {}
	
	default void unlistenForChanges(Runnable listener) {}
}
