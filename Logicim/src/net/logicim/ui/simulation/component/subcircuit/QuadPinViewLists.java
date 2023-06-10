package net.logicim.ui.simulation.component.subcircuit;

import net.logicim.common.type.Float2D;
import net.logicim.data.circuit.SubcircuitPinAlignment;
import net.logicim.data.circuit.SubcircuitPinAnchour;
import net.logicim.ui.common.Fonts;
import net.logicim.ui.shape.text.TextView;
import net.logicim.ui.simulation.component.passive.pin.PinPropertyHelper;
import net.logicim.ui.simulation.component.passive.pin.PinView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.awt.Font.SANS_SERIF;
import static net.logicim.data.circuit.SubcircuitPinAlignment.*;

public class QuadPinViewLists
{
  public PinViewLists leftViewLists;
  public PinViewLists rightViewLists;
  public PinViewLists topViewLists;
  public PinViewLists bottomViewLists;

  public QuadPinViewLists(PinPropertyHelper helper)
  {
    Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> pinsLocations = helper.groupPinsByLocation();
    leftViewLists = createPinViewLists(pinsLocations, LEFT);
    rightViewLists = createPinViewLists(pinsLocations, RIGHT);
    topViewLists = createPinViewLists(pinsLocations, TOP);
    bottomViewLists = createPinViewLists(pinsLocations, BOTTOM);
  }

  private PinViewLists createPinViewLists(Map<SubcircuitPinAlignment, Map<SubcircuitPinAnchour, List<PinView>>> pinsLocations, SubcircuitPinAlignment alignment)
  {
    Map<SubcircuitPinAnchour, List<PinView>> anchourMap = pinsLocations.get(alignment);

    List<PinView> centerPinViews = orderCenterPins(findAndSortPins(anchourMap, SubcircuitPinAnchour.CENTER));
    List<PinView> negativePinViews = orderNegativePins(findAndSortPins(anchourMap, SubcircuitPinAnchour.NEGATIVE));
    List<PinView> positivePinViews = orderPositivePins(findAndSortPins(anchourMap, SubcircuitPinAnchour.POSITIVE));

    Font font = Fonts.getInstance().getFont(SANS_SERIF, 0, 10, false);

    float maxWidest = 0;
    float widest = getWidest(centerPinViews, font);
    if (widest > maxWidest)
    {
      maxWidest = widest;
    }
    widest = getWidest(negativePinViews, font);
    if (widest > maxWidest)
    {
      maxWidest = widest;
    }
    widest = getWidest(positivePinViews, font);
    if (widest > maxWidest)
    {
      maxWidest = widest;
    }

    return new PinViewLists(negativePinViews, centerPinViews, positivePinViews, maxWidest);
  }

  private float getWidest(List<PinView> pinViews, Font font)
  {
    float widest = 0;
    for (PinView pinView : pinViews)
    {
      String text = pinView.getName();
      Float2D textDimension = TextView.calculateTextDimension(font, text);
      if (textDimension.x > widest)
      {
        widest = textDimension.x;
      }
    }
    return widest;
  }

  private List<PinView> findAndSortPins(Map<SubcircuitPinAnchour, List<PinView>> anchourMap, SubcircuitPinAnchour anchour)
  {
    List<PinView> pinViews;
    if (anchourMap != null)
    {
      pinViews = anchourMap.get(anchour);
    }
    else
    {
      pinViews = null;
    }

    List<PinView> sortedPinViews;
    if (pinViews != null)
    {
      sortedPinViews = new ArrayList<>(pinViews);
      Collections.sort(sortedPinViews);
    }
    else
    {
      sortedPinViews = new ArrayList<>();
    }
    return sortedPinViews;
  }

  private List<PinView> orderNegativePins(List<PinView> sortedPinViews)
  {
    return sortedPinViews;
  }

  private List<PinView> orderPositivePins(List<PinView> sortedPinViews)
  {
    Collections.reverse(sortedPinViews);
    return sortedPinViews;
  }

  private List<PinView> orderCenterPins(List<PinView> sortedPinViews)
  {
    if (sortedPinViews.size() > 0)
    {
      PinView[] orderedPinViews = new PinView[sortedPinViews.size()];

      int yEven = (sortedPinViews.size() - 1) / 2;
      int yOdd = yEven + 1;
      boolean even = true;
      for (PinView pinView : sortedPinViews)
      {
        int index;
        if (even)
        {
          index = yEven;
          yEven--;
        }
        else
        {
          index = yOdd;
          yOdd++;
        }
        orderedPinViews[index] = pinView;
        even = !even;
      }

      for (int i = 0; i < orderedPinViews.length; i++)
      {
        PinView orderedPinView = orderedPinViews[i];
        sortedPinViews.set(i, orderedPinView);
      }
    }
    return sortedPinViews;
  }
}

