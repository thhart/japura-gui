package org.japura.gui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Copyright (C) 2010-2015 Carlos Eduardo Leite de Andrade
 * <P>
 * This library is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * <P>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <P>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <A
 * HREF="www.gnu.org/licenses/">www.gnu.org/licenses/</A>
 * <P>
 * For more information, contact: <A HREF="www.japura.org">www.japura.org</A>
 * <P>
 *
 * @author Carlos Eduardo Leite de Andrade
 */
public class TitlePanel extends JComponent {

  private static final long serialVersionUID = 4L;

  private TitleBar titleBar;
  private JPanel innerPanel;
  private JPanel contentContainer;
  private int separatorThickness = 1;

  private JLabel titleIcon;

  private Component contentView;

  private Color separatorColor = new Color(0, 0, 0, 80);

  public TitlePanel(String title) {
    this(null, title, null);
  }

  public TitlePanel(Icon icon, String title) {
    this(icon, title, null);
  }

  public TitlePanel(String title, JComponent[] titleComponents) {
    this(null, title, titleComponents);
  }

  public TitlePanel(Icon icon, String title, JComponent[] titleComponents) {
    if (icon != null) {
      this.titleIcon = new JLabel(icon);
    }

    this.titleBar = new TitleBar(icon, title, titleComponents);

    this.contentContainer = new JPanel();
    this.contentContainer.setLayout(new BorderLayout());
    this.contentContainer.setBackground(Color.WHITE);

    this.innerPanel = new JPanel();

    this.innerPanel.setBorder(BorderFactory.createLineBorder(separatorColor));
    this.innerPanel.setLayout(new MigLayout("ins 0 0 0 0, wrap 1", "grow",
      "[]0[grow]"));
    this.innerPanel.add(titleBar, "grow x");
    this.innerPanel.add(contentContainer, "grow");

    super.add(innerPanel);
    if (this.titleIcon != null) {
      super.add(this.titleIcon);
    }

    setOpaque(false);

    updateZOrders();
  }

  @Override
  public Dimension getPreferredSize() {
    if (isPreferredSizeSet()) {
      return super.getPreferredSize();
    }

    Dimension dim = this.innerPanel.getPreferredSize();

    if (this.titleIcon != null) {
      int iconHeight = this.titleIcon.getPreferredSize().height;
      Insets insets = this.innerPanel.getInsets();
      int yBase = getTitleBar().getIconBase().getY();
      int yBaseToTop = yBase + insets.top;
      dim.height += Math.max(0, iconHeight - yBaseToTop);
    }

    return dim;
  }

  public TitleBar getTitleBar() {
    return titleBar;
  }

  @Override
  public void doLayout() {
    if (this.titleIcon != null) {
      Insets insets = this.innerPanel.getInsets();
      Dimension titleBarDim = getTitleBar().getPreferredSize();
      int iconHeight = this.titleIcon.getPreferredSize().height;
      int yBase = titleBarDim.height - getTitleBar().getTitleMargin().bottom;
      int yBaseToTop = yBase + insets.top;
      int outsideIconHeight = Math.max(0, (iconHeight - yBaseToTop));
      int y = outsideIconHeight;
      int availableHeight = getHeight() - y;
      this.innerPanel.setSize(getWidth(), availableHeight);
      this.innerPanel.setLocation(0, y);
      y =
        y + insets.top
          + (titleBarDim.height - getTitleBar().getTitleMargin().bottom)
          - iconHeight;
      this.titleIcon.setSize(this.titleIcon.getPreferredSize());
      int x = insets.left + this.titleBar.getTitleMargin().left;
      this.titleIcon.setLocation(x, y);
    }
    else {
      this.innerPanel.setSize(getWidth(), getHeight());
      this.innerPanel.setLocation(0, 0);
    }
  }

  @Override
  public final void setBorder(Border border) {
    this.innerPanel.setBorder(border);
  }

  private void updateZOrders() {
    int z = 0;
    if (titleIcon != null) {
      setComponentZOrder(titleIcon, z++);
    }
    setComponentZOrder(innerPanel, z++);
  }

  /**
   * Creates a separator line border between title and view with the specified
   * width.
   *
   * @param separatorThickness an integer specifying the width in pixels
   */
  public void setSeparatorThickness(int separatorThickness) {
    this.separatorThickness = Math.max(0, separatorThickness);
    updateContentContainer();
  }

  public int getSeparatorThickness() {
    return separatorThickness;
  }

  @Override
  public void remove(Component comp) {
    if (this.contentView.equals(comp)) {
      removeContentView();
    }
  }

  @Override
  public void remove(int index) {
  }

  @Override
  public void removeAll() {
    removeContentView();
  }

  private void updateContentContainer() {
    this.contentContainer.removeAll();
    if (this.contentView != null) {
      this.contentContainer.add(this.contentView);
      if (getSeparatorThickness() > 0) {
        this.contentContainer.setBorder(BorderFactory.createMatteBorder(
          getSeparatorThickness(), 0, 0, 0, getSeparatorColor()));
      }
      else {
        this.contentContainer.setBorder(null);
      }
    }
    else {
      this.contentContainer.setBorder(null);
    }
    this.contentContainer.revalidate();
  }

  public void removeContentView() {
    if (this.contentView != null) {
      this.contentView = null;
      updateContentContainer();
    }
  }

  public void setContentView(Component component) {
    if (component != null) {
      this.contentView = component;
      updateContentContainer();
    }
  }

  @Override
  public Component add(Component comp, int index) {
    setContentView(comp);
    return comp;
  }

  @Override
  public void add(Component comp, Object constraints, int index) {
    setContentView(comp);
  }

  @Override
  public void add(Component comp, Object constraints) {
    setContentView(comp);
  }

  @Override
  public Component add(Component comp) {
    setContentView(comp);
    return comp;
  }

  @Override
  public Component add(String name, Component comp) {
    setContentView(comp);
    return comp;
  }

  public Color getSeparatorColor() {
    return separatorColor;
  }

  public void setSeparatorColor(Color separatorColor) {
    if (separatorColor != null) {
      this.separatorColor = separatorColor;
      updateContentContainer();
    }
  }

  @Override
  public Font getFont() {
    return getTitleBar().getTitleLabel().getFont();
  }

  @Override
  public void setFont(Font font) {
    getTitleBar().getTitleLabel().setFont(font);
  }

  public void setTitle(String title) {
    getTitleBar().setTitle(title);
  }

  public String getTitle() {
    return getTitleBar().getTitle();
  }

  public void setTitleForeground(Color color) {
    getTitleBar().setForeground(color);
  }

  public Color getTitleForeground() {
    return getTitleBar().getForeground();
  }

  public void setTitleBackground(Color color) {
    getTitleBar().setTitleBackground(color);
  }

  public void setTitleBackground(Gradient gradient) {
    getTitleBar().setTitleBackground(gradient);
  }

  public Object getTitleBackground() {
    return getTitleBar().getTitleBackground();
  }

}
