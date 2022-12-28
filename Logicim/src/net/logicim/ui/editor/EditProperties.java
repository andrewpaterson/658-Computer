package net.logicim.ui.editor;

import net.logicim.common.reflect.InstanceInspector;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.DiscreteProperties;
import net.logicim.ui.common.integratedcircuit.DiscreteView;
import net.logicim.ui.components.table.Table;
import net.logicim.ui.components.table.TableModel;
import net.logicim.ui.components.table.TableValue;
import net.logicim.ui.components.button.Button;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.util.GridBagUtil;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.common.util.StringUtil.javaNameToHumanReadable;
import static net.logicim.ui.util.ButtonUtil.buildButtons;

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
      Dimension dimension = new Dimension(640, 480);
      dialog.setSize(dimension);
      mousePosition.x -= 50;
      mousePosition.y -= 50;
      dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dimension));

      Container contentPane = dialog.getContentPane();
      contentPane.setLayout(new GridBagLayout());

      JPanel topPanel = new JPanel(new GridBagLayout());
      contentPane.add(topPanel, GridBagUtil.gridBagConstraints(0, 0, 1, 1, BOTH));

      DiscreteProperties properties = discreteView.getProperties();
      InstanceInspector instanceInspector = new InstanceInspector(properties);
      List<Field> fields = new ArrayList<>(instanceInspector.getFields());
      Collections.reverse(fields);

      Table table = new Table();
      table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
      TableModel model = table.getModel();
      model.addColumn("Property");
      model.addColumn("Value");
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
        model.addRow(new Object[]{new TableValue(name), new TableValue(fieldValue)});
      }

      Vector<Vector> dataVector = model.getDataVector();

      topPanel.add(new JScrollPane(table), GridBagUtil.gridBagConstraints(0, 0, 1, 1, BOTH));
      topPanel.setBorder(new EmptyBorder(5, 5, 0, 5));

      JPanel bottomPanel = new JPanel();
      contentPane.add(bottomPanel, GridBagUtil.gridBagConstraints(0, 1, 0, 0, BOTH));

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

