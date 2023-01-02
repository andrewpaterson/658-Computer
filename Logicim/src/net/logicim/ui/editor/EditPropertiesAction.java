package net.logicim.ui.editor;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.common.type.Int2D;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.DiscreteProperties;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.ButtonAction;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.integratedcircuit.factory.ViewFactory;
import net.logicim.ui.integratedcircuit.factory.ViewFactoryStore;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EditPropertiesAction
    extends SimulatorEditorAction
    implements ButtonAction
{
  protected JFrame parentFrame;
  protected PropertiesPanel propertiesPanel;
  protected JDialog dialog;
  protected DiscreteView<?> discreteView;

  public EditPropertiesAction(SimulatorEditor editor, JFrame parentFrame)
  {
    super(editor);
    this.parentFrame = parentFrame;
  }

  @Override
  public void executeButtonAction()
  {
    DiscreteProperties properties = discreteView.getProperties();
    InstanceInspector instanceInspector = new InstanceInspector(properties);
    Map<Field, Object> map = propertiesPanel.getProperties();
    for (Map.Entry<Field, Object> entry : map.entrySet())
    {
      Field field = entry.getKey();
      Object value = entry.getValue();
      instanceInspector.setFieldValue(field, coerce(value, field.getType()));
    }

    CircuitEditor circuitEditor = editor.getCircuitEditor();

    Rotation rotation = discreteView.getRotation();
    Int2D position = discreteView.getPosition();

    Class<? extends DiscreteView<?>> aClass = (Class<? extends DiscreteView<?>>) discreteView.getClass();
    ViewFactory viewFactory = ViewFactoryStore.getInstance().get(aClass);
    DiscreteView<?> newDiscreteView = viewFactory.create(circuitEditor, position, rotation, properties);

    circuitEditor.deleteDiscreteView(this.discreteView);
    circuitEditor.placeDiscreteView(newDiscreteView);

    dialog.setVisible(false);
    dialog.dispose();
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

  @Override
  public void executeEditorAction()
  {
    discreteView = editor.getHoverDiscreteView();
    if (discreteView == null)
    {
      discreteView = editor.getSingleSelectionDiscreteView();
    }
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();

    if (discreteView != null)
    {
      dialog = new JDialog(parentFrame, discreteView.getType() + " Properties", true);
      Dimension dimension = new Dimension(360, 320);
      dialog.setSize(dimension);
      mousePosition.x -= 50;
      mousePosition.y -= 50;
      dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dimension));

      Container contentPane = dialog.getContentPane();
      contentPane.setLayout(new GridBagLayout());

      DiscreteProperties properties = discreteView.getProperties();
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
}

