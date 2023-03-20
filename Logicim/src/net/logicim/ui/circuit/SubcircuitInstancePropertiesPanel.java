package net.logicim.ui.circuit;

import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.subcircuit.SubcircuitInstanceProperties;
import net.logicim.ui.components.Label;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.components.typeeditor.ComboBoxPropertyEditor;
import net.logicim.ui.components.typeeditor.IntegerPropertyEditor;
import net.logicim.ui.components.typeeditor.TextPropertyEditor;
import net.logicim.ui.property.PropertiesPanel;
import net.logicim.ui.property.RotationEditor;
import net.logicim.ui.property.RotationEditorHolder;

import java.awt.*;
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
    super(new GridBagLayout());

    this.dialog = dialog;

    Form form = new Form();
    this.typeName = new ComboBoxPropertyEditor(this, TYPE_NAME, allowedSubcircuitTypeNames, properties.subcircuitTypeName);
    this.name = new TextPropertyEditor(this, NAME, properties.name);
    this.rotation = new RotationEditor(this, ROTATION, rotation);
    this.width = new IntegerPropertyEditor(this, WIDTH, properties.width);
    this.height = new IntegerPropertyEditor(this, HEIGHT, properties.height);
    this.comment = new TextPropertyEditor(this, COMMENT, properties.comment);

    form.addComponents(new Label(TYPE_NAME), this.typeName.getComponent());
    form.addComponents(new Label(NAME), this.name.getComponent());
    form.addComponents(new Label(ROTATION), this.rotation.getComponent());
    form.addComponents(new Label(WIDTH), this.width.getComponent());
    form.addComponents(new Label(HEIGHT), this.height.getComponent());
    form.addComponents(new Label(COMMENT), this.comment.getComponent());

    addPropertyFormView(form);
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

  public SubcircuitInstanceProperties createProperties()
  {
    return new SubcircuitInstanceProperties(name.getValue(),
                                            typeName.getValue(),
                                            comment.getValue(),
                                            width.getValue(),
                                            height.getValue());
  }
}

