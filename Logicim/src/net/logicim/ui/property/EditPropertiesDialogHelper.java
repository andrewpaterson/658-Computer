package net.logicim.ui.property;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;

public class EditPropertiesDialogHelper
{
  public EditPropertiesDialogHelper()
  {
  }

  public void showPropertyEditorDialog(JFrame parentFrame,
                                       SimulatorEditor editor,
                                       ComponentView<?> componentView)
  {
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
    Class<? extends ComponentView<?>> aClass = (Class<? extends ComponentView<?>>) componentView.getClass();
    ViewFactory<?, ?> viewFactory = ViewFactoryStore.getInstance().get(aClass);
    PropertyEditorDialog dialog = viewFactory.createEditorDialog(parentFrame, editor, componentView);
    dialog.build();
    mousePosition.x -= 50;
    mousePosition.y -= 50;
    dialog.setLocation(WindowSizer.ensureOnScreen(mousePosition, dialog.getDimension()));

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

