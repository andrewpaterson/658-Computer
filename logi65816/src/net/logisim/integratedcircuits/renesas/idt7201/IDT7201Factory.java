package net.logisim.integratedcircuits.renesas.idt7201;

import net.integratedcircuits.renesas.idt7201.IDT7201;
import net.logisim.common.*;
import net.util.StringUtil;

import java.awt.*;

import static net.logisim.common.PortPosition.LEFT;
import static net.logisim.common.PortPosition.RIGHT;

public class IDT7201Factory
    extends PropagatingInstanceFactory<IDT7201LogisimPins>
    implements LogisimPainter<IDT7201LogisimPins>
{
  protected static LogiPin PORT_WRITE;
  protected static LogiPin PORT_READ;
  protected static LogiPin PORT_RESET;
  protected static LogiPin PORT_FL_RT;
  protected static LogiPin PORT_XI;
  protected static LogiBus PORT_D;

  protected static LogiPin PORT_FULL;
  protected static LogiPin PORT_EMPTY;
  protected static LogiPin PORT_XO_HF;
  protected static LogiBus PORT_Q;

  public static IDT7201Factory create()
  {

    PortFactory factory = new PortFactory();

    PORT_WRITE = factory.inputShared("W", LEFT).setTooltip("Write (input: low latch from D)").setInverting().setDrawBar().createPin(15);
    PORT_READ = factory.inputShared("R", LEFT).setTooltip("Read (input: output onto Q)").setInverting().setDrawBar().createPin(15);
    PORT_RESET = factory.inputShared("RS", LEFT).setTooltip("Reset (input: low reset)").setInverting().setDrawBar().createPin(15);
    PORT_FL_RT = factory.inputShared("RT", LEFT).setTooltip("First Load or Retransmit (input)").setInverting().setDrawBar().createPin(15);
    PORT_XI = factory.inputShared("XI", LEFT).setTooltip("Expansion In (input: low to set Single Device Mode otherwise connect to previous device XO)").setInverting().setDrawBar().createPin(15);
    PORT_D = factory.inputShared("D", 9, LEFT).setTooltip("Data In").createBus(15);

    PORT_FULL = factory.inputShared("FF", RIGHT).setTooltip("Full (output: low when full)").setInverting().setDrawBar().createPin(15);
    PORT_EMPTY = factory.inputShared("EF", RIGHT).setTooltip("Empty (output: low when empty)").setInverting().setDrawBar().createPin(15);
    PORT_XO_HF = factory.inputShared("HF", RIGHT).setTooltip("Expansion Out or Half-full (output)").setInverting().setDrawBar().createPin(15);
    factory.blank(RIGHT);
    factory.blank(RIGHT);
    PORT_Q = factory.inputShared("Q", 9, RIGHT).setTooltip("Data Out").createBus(15);

    return new IDT7201Factory(new ComponentDescription(IDT7201.class.getSimpleName(),
                                                       IDT7201.TYPE,
                                                       240,
                                                       factory.getPorts(),
                                                       factory.getCommonPortNames()));
  }

  private IDT7201Factory(ComponentDescription description)
  {
    super(description);
  }

  @Override
  public void paint(IDT7201LogisimPins instance, Graphics2D graphics2D)
  {
    IDT7201 fifo = instance.getIntegratedCircuit();
    drawField(graphics2D, getTopOffset(0), WIDTH_16BIT, "Values:", fifo.getSizeAsString(), true);

    Color oldColour = graphics2D.getColor();

    int y = description.getTopYPlusMargin() + getTopOffset(1);
    int rectangleWidth = 144;
    int x = -rectangleWidth / 2 + 12;

    graphics2D.setColor(Color.lightGray);
    int height = 59;
    graphics2D.fillRect(x - 24, y - 5, rectangleWidth + 24, height);

    graphics2D.setColor(Color.black);
    graphics2D.drawRect(x, y - 5, rectangleWidth, height);

    Font oldFont = graphics2D.getFont();
    graphics2D.setFont(oldFont.deriveFont(Font.PLAIN));

    int size = fifo.size();
    int xi = 0;
    for (int i = 0; i < size; i++)
    {
      char c = fifo.get(i);
      if (c != (char) -1)
      {
        String s = StringUtil.to12BitHex(c);
        graphics2D.drawString(s, x + xi * 24 + 2, y + 8);
        xi++;
        if (xi > 5)
        {
          xi = 0;
          y += 14;

          if (y > 33)
          {
            break;
          }
        }
      }
    }

    y = description.getTopYPlusMargin() + getTopOffset(1);
    for (int i = 0; i < 4; i++)
    {
      String s = Integer.toString(i * 6);
      graphics2D.drawString(StringUtil.pad(2 - s.length(), "0") + s, x - 16, y + 8);
      y += 14;
    }

    graphics2D.setColor(oldColour);
    graphics2D.setFont(oldFont);
  }

  @Override
  protected IDT7201LogisimPins createInstance()
  {
    IDT7201LogisimPins pins = new IDT7201LogisimPins();
    new IDT7201("", pins);
    return pins;
  }
}

