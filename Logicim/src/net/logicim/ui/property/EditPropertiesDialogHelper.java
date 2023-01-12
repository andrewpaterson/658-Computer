package net.logicim.ui.property;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.common.type.Int2D;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.ButtonAction;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class EditPropertiesDialogHelper
    implements ButtonAction
{
  protected JFrame parentFrame;
  protected SimulatorEditor editor;

  protected PropertiesPanel propertiesPanel;
  protected JDialog dialog;
  protected ComponentView<?> componentView;

  public EditPropertiesDialogHelper(JFrame parentFrame, SimulatorEditor editor, ComponentView<?> componentView)
  {
    this.parentFrame = parentFrame;
    this.editor = editor;
    this.componentView = componentView;
  }

  @Override
  public void executeButtonAction()
  {
    Map<Field, Object> map = propertiesPanel.getProperties();

    boolean propertyChanged = updateProperties(map, componentView.getProperties());

    if (propertyChanged)
    {
      this.componentView.clampProperties();
      ComponentView<?> componentView = recreateComponentView(this.componentView.getProperties());
      editor.replaceSelection(componentView, this.componentView);
      editor.pushUndo();
    }

    dialog.setVisible(false);
    dialog.dispose();
  }

  protected boolean updateProperties(Map<Field, Object> map, ComponentProperties properties)
  {
    InstanceInspector instanceInspector = new InstanceInspector(properties);
    boolean propertyChanged = false;
    for (Map.Entry<Field, Object> entry : map.entrySet())
    {
      Field field = entry.getKey();
      Object newValue = entry.getValue();
      Object oldValue = instanceInspector.getFieldValue(field);

      if (!Objects.equals(newValue, oldValue))
      {
        propertyChanged = true;
        instanceInspector.setFieldValue(field, coerce(newValue, field.getType()));
      }
    }
    return propertyChanged;
  }

  protected ComponentView<?> recreateComponentView(ComponentProperties properties)
  {
    CircuitEditor circuitEditor = editor.getCircuitEditor();

    Rotation rotation = componentView.getRotation();
    Int2D position = componentView.getPosition();

    Class<? extends ComponentView<?>> aClass = (Class<? extends ComponentView<?>>) componentView.getClass();
    ViewFactory viewFactory = ViewFactoryStore.getInstance().get(aClass);
    ComponentView<?> newSemiconductorView = viewFactory.create(circuitEditor, position, rotation, properties);

    circuitEditor.deleteComponentView(this.componentView);
    circuitEditor.placeComponentView(newSemiconductorView);

    return newSemiconductorView;
  }

  protected Object coerce(Object value, Class<?> type)
  {
    if (value == null)
    {
      return null;
    }

    if (value instanceof Long)
    {
      if (type.equals(Integer.class) || type.equals(int.class))
      {
        return ((Long) value).intValue();
      }
    }

    if (value instanceof Double)
    {
      if (type.equals(Float.class) || type.equals(float.class))
      {
        return ((Double) value).floatValue();
      }
    }

    if (value instanceof Integer)
    {
      if (type.equals(Long.class) || type.equals(long.class))
      {
        return ((Integer) value).longValue();
      }
    }

    if (value instanceof Float)
    {
      if (type.equals(Double.class) || type.equals(double.class))
      {
        return ((Float) value).doubleValue();
      }
    }

    return value;
  }

  public void showPropertyEditorDialog()
  {
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
    dialog = new JDialog(this.parentFrame, componentView.getType() + " Properties", true);
    Dimension dimension = new Dimension(360, 320);
    dialog.setSize(dimension);
    mousePosition.x -= 50;
    mousePosition.y -= 50;
    dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dimension));

    Container contentPane = dialog.getContentPane();
    contentPane.setLayout(new GridBagLayout());

    ComponentProperties properties = componentView.getProperties();
    propertiesPanel = new PropertiesPanel(properties);
    contentPane.add(propertiesPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

    JPanel bottomPanel = new JPanel();
    contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));

    buildButtons(bottomPanel, new ActionButton("Okay", this), new CancelButton("Cancel", dialog));

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        dialog.setVisible(true);
      }
    });
  }

}
