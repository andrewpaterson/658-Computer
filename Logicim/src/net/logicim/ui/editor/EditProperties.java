package net.logicim.ui.editor;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.DiscreteProperties;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.components.button.Button;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.components.form.Form;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.awt.GridBagConstraints.*;
import static net.logicim.common.util.StringUtil.javaNameToHumanReadable;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public class EditProperties
    extends SimulatorEditorAction
{
  protected JFrame frame;

  public EditProperties(SimulatorEditor editor, JFrame frame)
  {
    super(editor);
    this.frame = frame;
  }

  @Override
  public void execute()
  {
    DiscreteView<?> discreteView = editor.getHoverDiscreteView();
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();

    if (discreteView != null)
    {
      JDialog dialog = new JDialog(frame, discreteView.getType() + " Properties", true);
      Dimension dimension = new Dimension(360, 320);
      dialog.setSize(dimension);
      mousePosition.x -= 50;
      mousePosition.y -= 50;
      dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dimension));

      Container contentPane = dialog.getContentPane();
      contentPane.setLayout(new GridBagLayout());

      JPanel topPanel = new JPanel(new GridBagLayout());
      contentPane.add(topPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

      DiscreteProperties properties = discreteView.getProperties();
      InstanceInspector instanceInspector = new InstanceInspector(properties);
      List<Field> fields = new ArrayList<>(instanceInspector.getFields());
      Collections.reverse(fields);

      Form form = new Form();
      for (Field field : fields)
      {
        Object fieldValue = instanceInspector.getFieldValue(field);
        String name = field.getName();
        int index = name.indexOf('_');
        if (index != -1)
        {
          String unit = " " + name.substring(index + 1);
          name = name.substring(0, index);
          fieldValue = fieldValue.toString() + unit;
        }
        name = javaNameToHumanReadable(name);
        JTextField textField = new JTextField();
        textField.setText(String.valueOf(fieldValue));
        form.add(new JLabel(name+ ":"), textField);
      }

      topPanel.add(form, gridBagConstraints(0, 0, 1, 0, HORIZONTAL));
      topPanel.add(new JPanel(), gridBagConstraints(0, 1, 0, 1, VERTICAL));
      topPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

      JPanel bottomPanel = new JPanel();
      contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));

      buildButtons(bottomPanel, new Button("Okay"), new CancelButton("Cancel", dialog));

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

