package name.bizna.emu65816;

public class Pins
{
  private int address;                  //output
  private int data;                     //bi-directional
  private boolean read;                 //output
  private boolean abortB;               //input
  private boolean busEnable;            //input
  private boolean phi2;                 //input
  private boolean emulation;            //output
  private boolean irqB;                 //input
  private boolean nmiB;                 //input
  private boolean memoryLock;           //output
  private boolean mx;                   //output
  private boolean ready;                //bi-directional.  Also.  Fuck-it I'm treating this an output.
  private boolean resetB;               //input
  private boolean vectorPullB;          //output
  private boolean validProgramAddress;  //output
  private boolean validDataAddress;     //output

  public Pins()
  {
    this.address = 0;
    this.data = 0;
    this.read = true;
    this.abortB = true;
    this.busEnable = true;
    this.phi2 = true;
    this.emulation = true;
    this.irqB = true;
    this.nmiB = true;
    this.memoryLock = false;
    this.mx = true;
    this.ready = false;
    this.resetB = true;
    this.vectorPullB = true;
    this.validProgramAddress = false;
    this.validDataAddress = false;
  }

  public int getAddress()
  {
    return address;
  }

  public void setAddress(int address)
  {
    this.address = address;
  }

  public int getData()
  {
    return data;
  }

  public void setData(int data)
  {
    this.data = data;
  }

  public void setRead(boolean read)
  {

    this.read = read;
  }

  public boolean isRead()
  {
    return read;
  }

  public boolean isAbortB()
  {
    return abortB;
  }

  public void setAbortB(boolean abortB)
  {
    this.abortB = abortB;
  }

  public boolean isBusEnable()
  {
    return busEnable;
  }

  public void setBusEnable(boolean busEnable)
  {
    this.busEnable = busEnable;
  }

  public boolean getPhi2()
  {
    return phi2;
  }

  public void setPhi2(boolean phi2)
  {
    this.phi2 = phi2;
  }

  public boolean isEmulation()
  {
    return emulation;
  }

  public void setEmulation(boolean emulation)
  {
    this.emulation = emulation;
  }

  public boolean isIrqB()
  {
    return irqB;
  }

  public void setIrqB(boolean irqB)
  {
    this.irqB = irqB;
  }

  public boolean isNmiB()
  {
    return nmiB;
  }

  public void setNmiB(boolean nmiB)
  {
    this.nmiB = nmiB;
  }

  public boolean isMemoryLock()
  {
    return memoryLock;
  }

  public void setMemoryLock(boolean memoryLock)
  {
    this.memoryLock = memoryLock;
  }

  public boolean isMx()
  {
    return mx;
  }

  public void setMx(boolean mx)
  {
    this.mx = mx;
  }

  public boolean isReady()
  {
    return ready;
  }

  public void setReady(boolean ready)
  {
    this.ready = ready;
  }

  public boolean isResetB()
  {
    return resetB;
  }

  public void setResetB(boolean resetB)
  {
    this.resetB = resetB;
  }

  public boolean isVectorPullB()
  {
    return vectorPullB;
  }

  public void setVectorPullB(boolean vectorPullB)
  {
    this.vectorPullB = vectorPullB;
  }

  public boolean isValidProgramAddress()
  {
    return validProgramAddress;
  }

  public void setValidProgramAddress(boolean validProgramAddress)
  {
    this.validProgramAddress = validProgramAddress;
  }

  public boolean isValidDataAddress()
  {
    return validDataAddress;
  }

  public void setValidDataAddress(boolean validDataAddress)
  {
    this.validDataAddress = validDataAddress;
  }
}

