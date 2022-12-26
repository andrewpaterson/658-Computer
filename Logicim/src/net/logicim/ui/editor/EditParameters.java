package net.logicim.ui.editor;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.DiscreteView;
import net.logicim.ui.components.Button;
import net.logicim.ui.util.GridBagUtil;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;

public class EditParameters
    extends SimulatorEditorAction
{
  protected JFrame frame;

  public EditParameters(SimulatorEditor editor, JFrame frame)
  {
    super(editor);
    this.frame = frame;
  }

  @Override
  public void execute()
  {
    DiscreteView discreteView = editor.getHoverDiscreteView();

    Point mousePosition = MouseInfo.getPointerInfo().getLocation();

    if (discreteView != null)
    {
      JDialog dialog = new JDialog(frame, discreteView.getType() + " Properties", true);
      Dimension dimension = new Dimension(1280, 640);
      dialog.setSize(dimension);
      mousePosition.x -= 50;
      mousePosition.y -= 50;
      dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dimension));

      Container contentPane = dialog.getContentPane();
      contentPane.setLayout(new GridBagLayout());

      JPanel topPanel = new JPanel();
      contentPane.add(topPanel, GridBagUtil.gridBagConstraints(0, 0, 1, 1, BOTH));
      DefaultTableModel model = new DefaultTableModel();
      JTable comp = new JTable(model);
      model.addColumn("Col1");
      model.addRow(new Object[] { "r1" });
      model.addRow(new Object[] { "r2" });
      model.addRow(new Object[] { "r3" });

      Vector<Vector> dataVector = model.getDataVector();

      topPanel.add(comp);

      JPanel bottomPanel = new JPanel();
      contentPane.add(bottomPanel, GridBagUtil.gridBagConstraints(0, 1, 0, 0, BOTH));

      buildButtons(bottomPanel, new Button("Okay"), new Button("Cancel"));

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

