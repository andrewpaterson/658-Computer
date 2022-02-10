package net.integratedcircuits.renesas.idt7201;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

public class IDT7201
    extends IntegratedCircuit<IDT7201Snapshot, IDT7201Pins>
{
  public static final String TYPE = "512 Byte FIFO";

  protected static int SIZE = 512;

  protected boolean prevRead;
  protected boolean prevWrite;

  protected char[] values;
  protected int head;
  protected int tail;

  protected char lastRead;

  public IDT7201(String name, IDT7201Pins pins)
  {
    super(name, pins);
    values = new char[SIZE];
    prevRead = true;
    prevWrite = true;
    lastRead = (char) -1;
  }

  @Override
  public void tick()
  {
    PinValue firstLoadBOrRetransmitBValue = getPins().getFLB_RTB();
    BusValue inputDValue = getPins().getInputD();
    PinValue readBValue = getPins().getRB();
    PinValue writeBValue = getPins().getWB();
    PinValue resetBValue = getPins().getRSB();
    PinValue expansionInValue = getPins().getXIB();
    boolean qImpedance = false;

    if (!expansionInValue.isLow())
    {
      getPins().setOutputError();
      getPins().setEFBError();
      getPins().setFFBError();
      getPins().setHFBError();
      return;
    }

    if (resetBValue.isLow())
    {
      if (!readBValue.isHigh() && !writeBValue.isHigh())
      {
        getPins().setOutputError();
        getPins().setEFBError();
        getPins().setFFBError();
        getPins().setHFBError();
        return;
      }
      qImpedance = true;
      head = 0;
      tail = 0;
    }

    boolean read = readBValue.isHigh();
    boolean readFalling = !read && prevRead;
    prevRead = read;
    boolean write = writeBValue.isHigh();
    boolean writeFalling = !write && prevWrite;
    prevWrite = write;

    if (readFalling)
    {
      if (!isEmpty())
      {
        lastRead = values[tail];
        tail++;
        if (tail == SIZE)
        {
          tail = 0;
        }
      }
      else
      {
        qImpedance = true;
      }
    }

    if (!read)
    {
      if (lastRead != (char) -1)
      {
        getPins().setOutputQ(lastRead);
      }
      else
      {
        qImpedance = true;
      }
    }
    else
    {
      lastRead = (char) -1;
      qImpedance = true;
    }

    if (writeFalling)
    {
      if (!isFull())
      {
        if (inputDValue.isValid())
        {
          long d = inputDValue.getValue();
          if (size() < SIZE)
          {
            values[head] = (char) d;
            head++;
            if (head == SIZE)
            {
              head = 0;
            }
          }
        }
      }
    }

    getPins().setEFB(!isEmpty());
    getPins().setFFB(!isFull());
    getPins().setXOB_HFB(size() < SIZE / 2);

    if (qImpedance)
    {
      getPins().setOutputHighImpedance();
    }
  }

  private boolean isFull()
  {
    return size() == SIZE - 1;
  }

  private boolean isEmpty()
  {
    return head == tail;
  }

  private int size()
  {
    if (head >= tail)
    {
      return head - tail;
    }
    else
    {
      return SIZE - tail + head;
    }
  }

  @Override
  public IDT7201Snapshot createSnapshot()
  {
    return new IDT7201Snapshot(prevRead, prevWrite, values, head, tail, lastRead);
  }

  @Override
  public void restoreFromSnapshot(IDT7201Snapshot snapshot)
  {
    prevRead = snapshot.prevRead;
    prevWrite = snapshot.prevWrite;
    head = snapshot.head;
    tail = snapshot.tail;
    values = snapshot.values;
    lastRead = snapshot.lastRead;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getSizeAsString()
  {
    return Integer.toString(size());
  }

  public String getFirstValueAsString()
  {
    if (!isEmpty())
    {
      long value;
      if (head > 1)
      {
        value = values[head - 1];
      }
      else
      {
        value = values[SIZE - 1];
      }

      return "0x" + StringUtil.to12BitHex(Math.toIntExact(value));
    }
    else
    {
      return "-----";
    }
  }

  public String getLastValueAsString()
  {
    if (!isEmpty())
    {
      long value = values[tail];
      return "0x" + StringUtil.to12BitHex(Math.toIntExact(value));
    }
    else
    {
      return "-----";
    }
  }
}

