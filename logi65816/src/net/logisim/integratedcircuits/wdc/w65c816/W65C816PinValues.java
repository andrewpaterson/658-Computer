package net.logisim.integratedcircuits.wdc.w65c816;

public class W65C816PinValues
{
  public boolean rwb;
  public boolean mlb;
  public boolean vpb;
  public int A;
  public boolean vda;
  public boolean vpa;

  public int readD;
  public int writeD;
  public int BA;

  public boolean reset;
  public boolean irq;
  public boolean nmi;
  public boolean rdy;

  public boolean abort;

  public boolean m;
  public boolean x;
  public boolean e;

  public W65C816PinValues()
  {
    reset = false;
    irq = false;
    nmi = false;
    abort = false;
  }

  public W65C816PinValues(W65C816PinValues pinValues)
  {
    this.rwb = pinValues.rwb;
    this.mlb = pinValues.mlb;
    this.vpb = pinValues.vpb;
    this.A = pinValues.A;
    this.vda = pinValues.vda;
    this.vpa = pinValues.vpa;
    this.readD = pinValues.readD;
    this.writeD = pinValues.writeD;
    this.BA = pinValues.BA;
    this.reset = pinValues.reset;
    this.irq = pinValues.irq;
    this.nmi = pinValues.nmi;
    this.rdy = pinValues.rdy;
    this.abort = pinValues.abort;
    this.m = pinValues.m;
    this.x = pinValues.x;
    this.e = pinValues.e;
  }
}

