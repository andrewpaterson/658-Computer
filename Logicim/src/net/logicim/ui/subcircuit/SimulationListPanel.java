package net.logicim.ui.subcircuit;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.Colours;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class SimulationListPanel
    extends JList<SubcircuitSimulation>
    implements SimulationListChangedListener
{
  public SimulationListPanel(ListSelectionListener simulatorFrame, Logicim editor)
  {
    super();
    SimulationListModel simulationListModel = new SimulationListModel(editor);
    setModel(simulationListModel);
    setMinimumSize(new Dimension(0, 0));
    setBackground(Colours.getInstance().getPanelBackground());
    addListSelectionListener(simulatorFrame);

    editor.addSimulationListChangedListener(this);

    InputMap inputMap = getInputMap();
    inputMap.setParent(new InputMap());
  }

  @Override
  public void simulationListChanged()
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        updateUI();

        SimulationListModel model = (SimulationListModel) getModel();
        int index = model.getSimulationIndex();

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

