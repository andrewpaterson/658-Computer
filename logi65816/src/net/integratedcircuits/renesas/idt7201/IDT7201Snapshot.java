package net.integratedcircuits.renesas.idt7201;

import net.common.Snapshot;

public class IDT7201Snapshot
    implements Snapshot
{
  public boolean prevRead;
  public boolean prevWrite;

  public  char[] values;
  public  int head;
  public  int tail;

  public char lastRead;

  public IDT7201Snapshot(boolean prevRead, boolean prevWrite, char[] values, int head, int tail, char lastRead)
  {
    this.prevRead = prevRead;
    this.prevWrite = prevWrite;
    this.values = new char[IDT7201.SIZE];
    System.arraycopy(values, 0, this.values, 0, IDT7201.SIZE);
    this.head = head;
    this.tail = tail;
    this.lastRead = lastRead;
  }
}


