//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.stendahls;

import com.stendahls.XUI.XUI;
import com.stendahls.XUI.XUIException;
import com.stendahls.util.resourceloader.ResourceLoader;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class XUIDialog extends JDialog {
    protected XUI xui;

    public XUIDialog() {
    }

    public XUIDialog(JFrame parent) throws XUIException {
        this(parent, true);
    }

    public XUIDialog(JDialog parent) throws XUIException {
        this(parent, true);
    }

    public XUIDialog(JFrame parent, boolean modal) throws XUIException {
        super(parent, modal);
    }

    public XUIDialog(JDialog parent, boolean modal) throws XUIException {
        super(parent, modal);
    }

    public XUIDialog(ResourceLoader rl, InputStream xmlStream, JFrame parent) throws XUIException {
        this(rl, xmlStream, parent, true);
    }

    public XUIDialog(ResourceLoader rl, InputStream xmlStream, JFrame parent, boolean modal) throws XUIException {
        super(parent, modal);
        this.init(rl, xmlStream);
    }

    public XUIDialog(ResourceLoader rl, InputStream xmlStream, JDialog parent) throws XUIException {
        this(rl, xmlStream, parent, true);
    }

    public XUIDialog(ResourceLoader rl, InputStream xmlStream, JDialog parent, boolean modal) throws XUIException {
        super(parent, modal);
        this.init(rl, xmlStream);
    }

    public void init(ResourceLoader rl, InputStream xmlStream) throws XUIException {
        this.xui = new XUI(rl, xmlStream);
        this.xui.setEventHandler(this);
        this.setTitle(this.xui.getTitle());
        this.getContentPane().add(this.xui, "Center");
        this.xui.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                XUIDialog.this.setVisible(false);
                XUIDialog.this.dispose();
            }
        }, KeyStroke.getKeyStroke(27, 0), 2);
    }

    public void display() {
        this.pack();
        Dimension ss = this.getParent().getSize();
        if(ss.width >= 80 && ss.height >= 80) {
            this.display(this.getParent().getLocation().x + ss.width / 2 - this.getWidth() / 2, this.getParent().getLocation().y + ss.height / 2 - this.getHeight() / 2);
        } else {
            ss = Toolkit.getDefaultToolkit().getScreenSize();
            this.display(ss.width / 2 - this.getWidth() / 2, ss.height / 2 - this.getHeight() / 2);
        }

    }

    public void display(int x, int y) {
        this.pack();
        this.setLocation(x, y);
        this.toFront();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                XUIDialog.this.toFront();
            }
        });
        this.setVisible(true);
    }

    public XUI getXUI() {
        return this.xui;
    }
}
