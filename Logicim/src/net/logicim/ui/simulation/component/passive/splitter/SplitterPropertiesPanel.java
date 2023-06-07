package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.passive.wire.SplitterProperties;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.PropertyClamp;
import net.logicim.ui.components.Label;
import net.logicim.ui.components.typeeditor.IntegerPropertyEditor;
import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;
import net.logicim.ui.property.RotationEditor;
import net.logicim.ui.property.RotationEditorHolder;
import net.logicim.ui.util.ButtonUtil;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

public class SplitterPropertiesPanel
    extends PropertiesPanel
    implements RotationEditorHolder
{
  public static final String ROTATION = "Rotation";
  public static final String NAME = "Name";
  public static final String FAN_OUT = "Fan Out";
  public static final String BIT_WIDTH = "Bit Width";
  public static final String APPEARANCE = "Appearance";
  public static final String SPACING = "Spacing";
  public static final String OFFSET = "Offset";
  public static final String BIT = "Bit ";

  protected RotationEditor rotation;
  protected TextPropertyEditor name;
  protected IntegerPropertyEditor fanOut;
  protected IntegerPropertyEditor bitWidth;
  protected SplitterAppearanceEditor appearance;
  protected IntegerPropertyEditor spacing;
  protected IntegerPropertyEditor offset;

  protected List<IntegerPropertyEditor> indices;
  protected SplitterPropertyEditorDialog dialog;

  public SplitterPropertiesPanel(SplitterPropertyEditorDialog dialog,
                                 SplitterProperties properties,
                                 Rotation rotation)
  {
    super();

    this.dialog = dialog;

    this.rotation = new RotationEditor(this, ROTATION, rotation);
    name = new TextPropertyEditor(this, NAME, properties.name);
    bitWidth = new IntegerPropertyEditor(this, BIT_WIDTH, properties.bitWidth);
    fanOut = new IntegerPropertyEditor(this, FAN_OUT, properties.fanOut);
    appearance = new SplitterAppearanceEditor(this, APPEARANCE, properties.appearance);
    spacing = new IntegerPropertyEditor(this, SPACING, properties.gridSpacing);
    offset = new IntegerPropertyEditor(this, OFFSET, properties.endOffset);

    addLabeledComponent(ROTATION, this.rotation.getComponent());
    addLabeledComponent(NAME, name.getComponent());
    addLabeledComponent(FAN_OUT, fanOut.getComponent());
    addLabeledComponent(BIT_WIDTH, bitWidth.getComponent());
    addLabeledComponent(SPACING, spacing.getComponent());
    addLabeledComponent(APPEARANCE, appearance.getComponent());
    form.addComponents(new Label(OFFSET), offset.getComponent(), 5);
    form.addComponent(ButtonUtil.buildButtons(105,
                                              new SplitterOrderButton(this, "Descending"),
                                              new SplitterOrderButton(this, "Ascending")));

    indices = new ArrayList<>(properties.bitWidth);
    for (int i = 0; i < properties.bitWidth; i++)
    {
      String name = BIT + i;
      IntegerPropertyEditor bitEditor = new IntegerPropertyEditor(this, name, properties.splitIndices[i]);
      form.addComponents(new Label(name), bitEditor.getComponent(), 0);
      indices.add(bitEditor);
    }
  }

  @Override
  public void focusLost(FocusEvent e)
  {
    dialog.focusLost();
  }

  public SplitterProperties valueMaybeChanged()
  {
    SplitterProperties properties = createProperties(dialog.getComponentView().getProperties());
    int bitWidthValue = bitWidth.getValue();
    if (!(bitWidthValue > 0 && bitWidthValue < PropertyClamp.MAX_WIDTH))
    {
      bitWidth.setText("1");
    }
    return properties;
  }

  @Override
  public SplitterProperties createProperties(ComponentProperties oldProperties)
  {
    int bitWidthValue = bitWidth.getValue();

    int[] splitIndices = new int[bitWidthValue];

    for (int i = 0; i < indices.size() && i < bitWidthValue; i++)
    {
      IntegerPropertyEditor index = indices.get(i);
      splitIndices[i] = index.getValue();
    }

    return new SplitterProperties(name.getValue(),
                                  bitWidthValue,
                                  fanOut.getValue(),
                                  spacing.getValue(),
                                  appearance.getValue(),
                                  offset.getValue(),
                                  splitIndices);
  }

  static void reverseArray(int[] array)
  {
    int size = array.length;
    for (int i = 0; i < size / 2; i++)
    {
      int temp = array[i];
      array[i] = array[size - i - 1];
      array[size - i - 1] = temp;
    }
  }

  public void orderFanOut(String buttonText)
  {
    int bitWidthValue = bitWidth.getValue();
    int[] splitIndices = SplitterProperties.createSplitIndices(bitWidthValue, fanOut.getValue());

    if (buttonText.equals("Descending"))
    {
      reverseArray(splitIndices);
    }

    for (int i = 0; i < splitIndices.length; i++)
    {
      int splitIndex = splitIndices[i];

      IntegerPropertyEditor index = indices.get(i);
      index.setText(Integer.toString(splitIndex));
    }
  }

  public Rotation getRotation()
  {
    return rotation.getValue();
  }
}

