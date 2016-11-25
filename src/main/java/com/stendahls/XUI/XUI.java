//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.stendahls;

import com.stendahls.XUI.ImageCache;
import com.stendahls.XUI.MenuItemDescriptionListener;
import com.stendahls.XUI.XUIElement;
import com.stendahls.XUI.XUIErrorDialogHelper;
import com.stendahls.XUI.XUIException;
import com.stendahls.ui.util.WaitCursor;
import com.stendahls.util.resourceloader.GeneralResourceLoader;
import com.stendahls.util.resourceloader.ResourceLoader;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Hashtable;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

public class XUI extends JPanel {
    private static final String[] MODIFIERS = new String[]{"shift", "ctrl", "meta", "alt"};
    private String title;
    private Image image;
    private Hashtable XUIcomponents;
    private Hashtable buttonGroups;
    private boolean markPanelDebug;
    private static ResourceLoader globalResourceLoader = new GeneralResourceLoader(XUI.class);
    private static ImageCache imageCache = new ImageCache();
    private ResourceLoader resourceLoader;
    private Object eventHandler;
    private MenuItemDescriptionListener menuItemDescriptionListener;
    private JMenuBar menuBar;
    private static boolean globalDebug;
    private Component wantInitialFocus;
    private Border raisedBorder;
    private Border loweredBorder;

    public XUI() {
        this.title = "";
        this.XUIcomponents = new Hashtable();
        this.buttonGroups = new Hashtable();
        this.raisedBorder = BorderFactory.createBevelBorder(0);
        this.loweredBorder = BorderFactory.createBevelBorder(1);
    }

    public XUI(InputStream in) throws XUIException {
        this(new InputSource(in));
    }

    public XUI(ResourceLoader rl, String s) throws XUIException {
        this.title = "";
        this.XUIcomponents = new Hashtable();
        this.buttonGroups = new Hashtable();
        this.raisedBorder = BorderFactory.createBevelBorder(0);
        this.loweredBorder = BorderFactory.createBevelBorder(1);
        this.init(rl, s);
    }

    public XUI(ResourceLoader rl, InputStream in) throws XUIException {
        this.title = "";
        this.XUIcomponents = new Hashtable();
        this.buttonGroups = new Hashtable();
        this.raisedBorder = BorderFactory.createBevelBorder(0);
        this.loweredBorder = BorderFactory.createBevelBorder(1);
        this.init(rl, new InputSource(in));
    }

    public XUI(Reader in) throws XUIException {
        this(new InputSource(in));
    }

    public XUI(InputSource in) throws XUIException {
        this.title = "";
        this.XUIcomponents = new Hashtable();
        this.buttonGroups = new Hashtable();
        this.raisedBorder = BorderFactory.createBevelBorder(0);
        this.loweredBorder = BorderFactory.createBevelBorder(1);
        this.init(in);
    }

    public void init(InputStream in) throws XUIException {
        this.init(new InputSource(in));
    }

    public void init(Reader in) throws XUIException {
        this.init(new InputSource(in));
    }

    public void init(InputSource in) throws XUIException {
        this.init((ResourceLoader)null, (InputSource)in);
    }

    public void init(ResourceLoader rl, String s) throws XUIException {
        try {
            this.init(rl, new InputSource(rl.getResourceStream(s)));
        } catch (Exception var4) {
            throw new XUIException("Could not load " + s, var4);
        }
    }

    public void init(ResourceLoader rl, InputSource in) throws XUIException {
        if(rl != null) {
            this.setResourceLoader(rl);
        }

        this.setLayout(new BoxLayout(this, 1));

        try {
            Document e = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
            NodeList se1 = e.getElementsByTagName("title");
            if(se1.getLength() > 0) {
                NamedNodeMap n = se1.item(0).getAttributes();
                this.title = n.getNamedItem("value").getNodeValue();
                if(n.getNamedItem("image") != null) {
                    this.image = Toolkit.getDefaultToolkit().getImage(this.getResourceLoader().getResource(n.getNamedItem("image").getNodeValue()));
                }
            }

            se1 = e.getElementsByTagName("menubar");
            Node n1;
            if(se1.getLength() > 0) {
                n1 = se1.item(0);
                this.menuBar = new JMenuBar();
                this.processSubElements(this.menuBar, n1);
            }

            se1 = e.getElementsByTagName("body");
            if(se1.getLength() > 0) {
                n1 = se1.item(0);
                if(n1.getAttributes().getNamedItem("markpanels") != null) {
                    this.markPanelDebug = true;
                }

                if(globalDebug) {
                    this.markPanelDebug = true;
                }

                this.processSubElements(this, n1);
            } else {
                throw new XUIException("No body tag in XUI xml! The UI must be contained such a tag.");
            }
        } catch (Exception var6) {
            if(var6 instanceof SAXParseException) {
                SAXParseException se = (SAXParseException)var6;
                throw new XUIException("Error while parsing XML on line " + se.getLineNumber() + '!', var6);
            } else {
                throw new XUIException("Error while initializing XML UI!", var6);
            }
        }
    }

    public void processSubElements(Container panel, Node topNode) throws XUIException {
        NodeList nl = topNode.getChildNodes();

        for(int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            if(n instanceof Element) {
                this.processElement(panel, n);
            }
        }

    }

    public XUIElement processElement(Container parent, Node topNode) throws XUIException {
        String classname = topNode.getNodeName().toLowerCase();
        classname = "com.stendahls.XUI.elements." + classname;

        Object xuiComponent;
        try {
            xuiComponent = Class.forName(classname).newInstance();
        } catch (Exception var9) {
            throw new XUIException("UI type \'" + classname + "\' could not be found/loaded!", var9);
        }

        try {
            XUIElement e = (XUIElement)xuiComponent;
            e.init(this, parent, topNode);
            Component comp = e.getComponent();
            if(comp != null && parent != null && e.wantsToBePartOfUITree()) {
                parent.add(comp);
            }

            if(comp instanceof JMenuItem) {
                JMenuItem var7 = (JMenuItem)comp;
            }

            return e;
        } catch (Exception var8) {
            throw new XUIException("Could not initialize component " + classname, var8);
        }
    }

    public XUIElement getXUIComponent(String id) {
        return (XUIElement)this.XUIcomponents.get(id);
    }

    public Component getComponent(String id) {
        return this.getXUIComponent(id) == null?null:this.getXUIComponent(id).getComponent();
    }

    public boolean containsComponent(String id) {
        return this.getXUIComponent(id) != null;
    }

    public void registerXUIComponent(XUIElement c) {
        if(c.getId() != null) {
            this.XUIcomponents.put(c.getId(), c);
        }

    }

    public Collection getXUIComponents() {
        return this.XUIcomponents.values();
    }

    public int getNComponents() {
        return this.XUIcomponents.size();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;

        Container c;
        for(c = this.getParent(); c != null && !(c instanceof XUI); c = c.getParent()) {
            ;
        }

        if(c != null) {
            ((XUI)c).setTitle(title);
        } else {
            for(c = this.getParent(); c != null && !(c instanceof Frame) && !(c instanceof Dialog); c = c.getParent()) {
                ;
            }

            if(c != null) {
                if(c instanceof Dialog) {
                    ((Dialog)c).setTitle(title);
                } else {
                    ((Frame)c).setTitle(title);
                }
            }
        }

    }

    public static ImageCache getImageCache() {
        return imageCache;
    }

    public ButtonGroup getButtonGroup(String id) {
        ButtonGroup bg = (ButtonGroup)this.buttonGroups.get(id);
        if(bg == null) {
            bg = new ButtonGroup();
            this.buttonGroups.put(id, bg);
        }

        return bg;
    }

    public void addNotify() {
        super.addNotify();
        if(this.wantInitialFocus != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    XUI.this.wantInitialFocus.requestFocusInWindow();
                }
            });
        }

    }

    public boolean isMarkPanelDebugOn() {
        return this.markPanelDebug;
    }

    public void registerActionListeners(ActionListener a, String[] actions) throws XUIException {
        for(int i = 0; i < actions.length; ++i) {
            String action = actions[i];
            Component c = this.getComponent(action);
            if(c == null) {
                throw new XUIException("There is no component with id " + action + " to bind action listener to.");
            }

            if(c instanceof AbstractButton) {
                ((AbstractButton)c).addActionListener(a);
            } else {
                if(!(c instanceof JComboBox)) {
                    throw new XUIException("Component " + action + " can\'t handle actions");
                }

                ((JComboBox)c).addActionListener(a);
            }
        }

    }

    public ResourceLoader getResourceLoader() {
        return this.resourceLoader != null?this.resourceLoader:globalResourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void setEventHandler(Object eventHandler) {
        this.eventHandler = eventHandler;
    }

    public Object getEventHandler() {
        return this.eventHandler;
    }

    public static void setGlobalDebug(boolean selected) {
        globalDebug = selected;
    }

    public int getDefaultBorderSize() {
        return 5;
    }

    public JMenuBar getMenuBar() {
        return this.menuBar;
    }

    public int getDefaultMarginSize() {
        return 2;
    }

    public static ResourceLoader getGlobalResourceLoader() {
        return globalResourceLoader;
    }

    public void setMenuItemDescriptionListener(MenuItemDescriptionListener menuItemDescriptionListener) {
        this.menuItemDescriptionListener = menuItemDescriptionListener;
    }

    public Image getImage() {
        return this.image;
    }

    public void setWantInitialFocus(Component wantInitialFocus) throws XUIException {
        if(this.wantInitialFocus != null) {
            throw new XUIException("Default focus component already set!");
        } else {
            this.wantInitialFocus = wantInitialFocus;
        }
    }

    public void setWaitCursor() {
        WaitCursor.set(this);
    }

    public void resetWaitCursor() {
        WaitCursor.reset(this);
    }

    public static KeyStroke createKeyStroke(String keyCodeString) throws XUIException {
        int modifiers = 0;

        boolean c;
        do {
            int keyCode;
            if(keyCodeString.indexOf(45) == -1) {
                Class var7 = KeyEvent.class;

                try {
                    Field e = var7.getField("VK_" + keyCodeString.toUpperCase());
                    keyCode = e.getInt(KeyEvent.class);
                } catch (NoSuchFieldException var5) {
                    throw new XUIException("Unknown key code " + keyCodeString);
                } catch (IllegalAccessException var6) {
                    throw new XUIException("Internal error while getting key code.", var6);
                }

                return KeyStroke.getKeyStroke(keyCode, modifiers);
            }

            c = false;

            for(keyCode = 0; keyCode < MODIFIERS.length; ++keyCode) {
                if(keyCodeString.startsWith(MODIFIERS[keyCode])) {
                    keyCodeString = keyCodeString.substring(MODIFIERS[keyCode].length() + 1);
                    modifiers |= 1 << keyCode;
                    c = true;
                    break;
                }
            }
        } while(c);

        throw new XUIException("Unknown keystroke modifier: " + keyCodeString);
    }

    public void propagateEvent(ActionEvent e, String id) {
        this.propagateEvent(this.getEventHandler(), e, id);
    }

    public void propagateEvent(Object eventHandler, ActionEvent e, String id) {
        Object eh = eventHandler;
        Object[] args = new Object[]{e};
        Class[] paramTypes = new Class[]{e.getClass()};
        if(id.indexOf(47) != -1) {
            String e1 = id.substring(id.indexOf(47) + 1);
            id = id.substring(0, id.indexOf(47));
            e.setSource(e1);
        }

        try {
            this.setWaitCursor();
            if(eh == null) {
                throw new XUIException("No event handler for XUI " + this.getTitle());
            }

            Method e12 = eh.getClass().getMethod("EVENT_" + id, paramTypes);

            try {
                e12.invoke(eh, args);
            } catch (InvocationTargetException var16) {
                throw new Exception("Method \'EVENT_" + id + "(...)\' in " + eh.getClass().getName() + " failed!", var16);
            }
        } catch (Exception var17) {
            Exception e11 = var17;
            if(var17 instanceof NoSuchMethodException) {
                e11 = new Exception("Event  " + id + " missing in " + eventHandler.getClass().getName() + "!");
            }

            Container c;
            for(c = this.getParent(); c != null && !(c instanceof JFrame) && !(c instanceof JDialog); c = c.getParent()) {
                ;
            }

            try {
                if(c != null) {
                    if(c instanceof JDialog) {
                        Class.forName(XUIErrorDialogHelper.getErrorDialogClass()).getConstructor(new Class[]{JDialog.class, Throwable.class, Boolean.TYPE}).newInstance(new Object[]{(JDialog)c, e11, Boolean.valueOf(false)});
                    } else {
                        Class.forName(XUIErrorDialogHelper.getErrorDialogClass()).getConstructor(new Class[]{JFrame.class, Throwable.class, Boolean.TYPE}).newInstance(new Object[]{(JFrame)c, e11, Boolean.valueOf(false)});
                    }
                } else {
                    Class.forName(XUIErrorDialogHelper.getErrorDialogClass()).getConstructor(new Class[]{Throwable.class, Boolean.TYPE}).newInstance(new Object[]{e11, Boolean.valueOf(false)});
                }
            } catch (Exception var15) {
                JOptionPane.showMessageDialog(c, e11, "Internal Error", 0);
            }
        } finally {
            this.resetWaitCursor();
        }

    }

    public Border getRaisedBorder() {
        return this.raisedBorder;
    }

    public void setRaisedBorder(Border raisedBorder) {
        this.raisedBorder = raisedBorder;
    }

    public Border getLoweredBorder() {
        return this.loweredBorder;
    }

    public void setLoweredBorder(Border loweredBorder) {
        this.loweredBorder = loweredBorder;
    }
}
