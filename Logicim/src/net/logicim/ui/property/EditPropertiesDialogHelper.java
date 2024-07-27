package net.logicim.ui.property;

import net.common.SimulatorException;
import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.StaticView;
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
                                       Logicim editor,
                                       StaticView<?> componentView)
  {
    Point mousePosition = MouseInfo.getPointerInfo().getLocation();
    Class<? extends StaticView<?>> aClass = (Class<? extends StaticView<?>>) componentView.getClass();
    ViewFactory<?, ?> viewFactory = ViewFactoryStore.getInstance().get(aClass);
    if (viewFactory == null)
    {
      throw new SimulatorException("Cannot find ViewFactory for class [%s]. Call  ViewFactoryStore.getInstance.addAll()");
    }
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

