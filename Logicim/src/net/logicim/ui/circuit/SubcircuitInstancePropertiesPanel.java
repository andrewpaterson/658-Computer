package net.logicim.ui.circuit;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.components.typeeditor.ComboBoxPropertyEditor;
import net.logicim.ui.components.typeeditor.IntegerPropertyEditor;
import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;
import net.logicim.ui.property.RotationEditor;
import net.logicim.ui.property.RotationEditorHolder;

import java.awt.event.FocusEvent;
import java.util.List;

public class SubcircuitInstancePropertiesPanel
    extends PropertiesPanel
    implements RotationEditorHolder
{
  public static final String NAME = "Name";
  public static final String TYPE_NAME = "Type Name";
  public static final String ROTATION = "Rotation";
  public static final String WIDTH = "Width";
  public static final String HEIGHT = "Height";
  public static final String COMMENT = "Comment";

  protected TextPropertyEditor name;
  protected ComboBoxPropertyEditor typeName;
  protected RotationEditor rotation;
  protected TextPropertyEditor comment;
  protected IntegerPropertyEditor width;
  protected IntegerPropertyEditor height;

  protected SubcircuitInstancePropertyEditorDialog dialog;

  public SubcircuitInstancePropertiesPanel(SubcircuitInstancePropertyEditorDialog dialog,
                                           SubcircuitInstanceProperties properties,
                                           List<String> allowedSubcircuitTypeNames,
                                           Rotation rotation)
  {
    super();

    this.dialog = dialog;

    this.typeName = new ComboBoxPropertyEditor(TYPE_NAME, allowedSubcircuitTypeNames, properties.subcircuitTypeName);
    this.name = new TextPropertyEditor(NAME, properties.name);
    this.rotation = new RotationEditor(ROTATION, rotation);
    this.width = new IntegerPropertyEditor(this, WIDTH, properties.width);
    this.height = new IntegerPropertyEditor(this, HEIGHT, properties.height);
    this.comment = new TextPropertyEditor(COMMENT, properties.comment);

    addLabeledComponent(TYPE_NAME, this.typeName.getComponent());
    addLabeledComponent(NAME, this.name.getComponent());
    addLabeledComponent(ROTATION, this.rotation.getComponent());
    addLabeledComponent(WIDTH, this.width.getComponent());
    addLabeledComponent(HEIGHT, this.height.getComponent());
    addLabeledComponent(COMMENT, this.comment.getComponent());
  }

  @Override
  public void focusLost(FocusEvent e)
  {
    dialog.focusLost();
  }

  @Override
  public Rotation getRotation()
  {
    return rotation.getValue();
  }

  public SubcircuitInstanceProperties createProperties(ComponentProperties oldProperties)
  {
    return new SubcircuitInstanceProperties(name.getValue(),
                                            typeName.getValue(),
                                            comment.getValue(),
                                            width.getValue(),
                                            height.getValue());
  }
}

