package net.logicim.ui.subcircuit;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.Colours;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class SubcircuitListPanel
    extends JList<SubcircuitEditor>
    implements SubcircuitListChangedListener
{
  public SubcircuitListPanel(ListSelectionListener simulatorFrame, Logicim editor)
  {
    SubcircuitListModel subcircuitListModel = new SubcircuitListModel(editor.getSubcircuitEditorList());
    setModel(subcircuitListModel);
    setMinimumSize(new Dimension(0, 0));
    setBackground(Colours.getInstance().getPanelBackground());
    addListSelectionListener(simulatorFrame);

    editor.addSubcircuitListChangedListener(this);
  }

  @Override
  public void subcircuitListChanged()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        updateUI();

        SubcircuitListModel model = (SubcircuitListModel) getModel();
        int index = model.getSubcircuitEditorIndex();

        ListSelectionListener[] listSelectionListeners = removeListSelectionListeners();
        setSelectedIndex(index);
        addListSelectionListeners(listSelectionListeners);
      }
    });
  }

  protected void addListSelectionListeners(ListSelectionListener[] listSelectionListeners)
  {
    for (ListSelectionListener listSelectionListener : listSelectionListeners)
    {
      addListSelectionListener(listSelectionListener);
    }
  }

  protected ListSelectionListener[] removeListSelectionListeners()
  {
    ListSelectionListener[] listSelectionListeners = getListSelectionListeners();
    for (ListSelectionListener listSelectionListener : listSelectionListeners)
    {
      removeListSelectionListener(listSelectionListener);
    }
    return listSelectionListeners;
  }
}

