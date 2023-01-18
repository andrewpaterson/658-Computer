package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.IntegerPropertyEditor;
import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class SplitterPropertiesPanel
    extends PropertiesPanel
{
  public static final String NAME = "Name";
  public static final String FAN_OUT = "Fan Out";
  public static final String BIT_WIDTH = "Bit Width";
  public static final String APPEARANCE = "Appearance";
  public static final String SPACING = "Spacing";
  public static final String OFFSET = "Offset";
  public static final String BIT = "Bit ";

  protected TextPropertyEditor name;
  protected IntegerPropertyEditor fanOut;
  protected IntegerPropertyEditor bitWidth;
  protected IntegerPropertyEditor appearance;
  protected IntegerPropertyEditor spacing;
  protected IntegerPropertyEditor offset;

  protected List<IntegerPropertyEditor> indices;
  protected SplitterPropertyEditorDialog dialog;

  public SplitterPropertiesPanel(SplitterPropertyEditorDialog dialog, SplitterProperties properties)
  {
    super(new GridBagLayout());
    this.dialog = dialog;

    Form form = new Form();
    name = new TextPropertyEditor(this, NAME, properties.name);
    fanOut = new IntegerPropertyEditor(this, FAN_OUT, properties.fanOut);
    bitWidth = new IntegerPropertyEditor(this, BIT_WIDTH, properties.bitWidth);
    appearance = new IntegerPropertyEditor(this, APPEARANCE, 0);
    spacing = new IntegerPropertyEditor(this, SPACING, properties.gridSpacing);
    offset = new IntegerPropertyEditor(this, OFFSET, properties.endOffset);

    form.addComponents(new Label(NAME), name.getComponent());
    form.addComponents(new Label(FAN_OUT), fanOut.getComponent());
    form.addComponents(new Label(BIT_WIDTH), bitWidth.getComponent());
    form.addComponents(new Label(APPEARANCE), appearance.getComponent());
    form.addComponents(new Label(SPACING), spacing.getComponent());
    form.addComponents(new Label(OFFSET), offset.getComponent(), 5);

    indices = new ArrayList<>(properties.bitWidth);
    for (int i = 0; i < properties.bitWidth; i++)
    {
      String name = BIT + i;
      IntegerPropertyEditor bitEditor = new IntegerPropertyEditor(this, name, 0);
      form.addComponents(new Label(name), bitEditor.getComponent(), 0);
      indices.add(bitEditor);
    }

    addPropertyFormView(form);
  }

  @Override
  public void focusLost(FocusEvent e)
  {
    SplitterProperties properties = createProperties();
    dialog.focusLost(properties);
  }

  public SplitterProperties createProperties()
  {
    int bitWidthValue = bitWidth.getValue();
    if (!(bitWidthValue >= 0 && bitWidthValue < 99))
    {
      bitWidthValue = 0;
    }

    int[] splitIndices = new int[bitWidthValue];

    for (int i = 0; i < indices.size() && i < bitWidthValue; i++)
    {
      IntegerPropertyEditor index = indices.get(i);
      splitIndices[i] = index.getValue();
    }

    return new SplitterProperties(name.getValue(),
                                  bitWidthValue,
                                  fanOut.getValue(),
                                  offset.getValue(),
                                  spacing.getValue(),
                                  splitIndices);
  }
}

