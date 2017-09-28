package net.leejjon.bluffpoker.ios;

import apple.coregraphics.struct.CGRect;
import apple.foundation.NSSet;
import apple.glkit.GLKView;
import apple.opengles.EAGLContext;
import apple.uikit.UIEvent;
import apple.uikit.UITouch;
import org.moe.natj.general.NatJ;
import org.moe.natj.general.Pointer;
import org.moe.natj.general.ann.ByValue;
import org.moe.natj.objc.ann.Selector;

public class BluffPokerIOSGLKView extends GLKView {
    private BluffPokerIOSGraphics graphics;

    static {
        NatJ.register();
    }

    @Selector("alloc")
    public static native BluffPokerIOSGLKView alloc ();

    @Selector("init")
    public native BluffPokerIOSGLKView init ();

    protected BluffPokerIOSGLKView(Pointer peer) {
        super(peer);
    }

    public BluffPokerIOSGLKView init (BluffPokerIOSGraphics graphics, CGRect bounds, EAGLContext context) {
        initWithFrameContext(bounds, context);
        this.graphics = graphics;
        return this;
    }

    @Override
    public void touchesBeganWithEvent (NSSet<? extends UITouch> nsSet, UIEvent uiEvent) {
        graphics.input.onTouch(nsSet);
    }

    @Override
    public void touchesCancelledWithEvent (NSSet<? extends UITouch> nsSet, UIEvent uiEvent) {
        graphics.input.onTouch(nsSet);
    }

    @Override
    public void touchesEndedWithEvent (NSSet<? extends UITouch> nsSet, UIEvent uiEvent) {
        graphics.input.onTouch(nsSet);
    }

    @Override
    public void touchesMovedWithEvent (NSSet<? extends UITouch> nsSet, UIEvent uiEvent) {
        graphics.input.onTouch(nsSet);
    }

    @Override
    public void drawRect (@ByValue CGRect cgRect) {
        graphics.glkViewDrawInRect(this, cgRect);
    }
}
