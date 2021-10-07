package name.bizna.cpu;

@SuppressWarnings({"FieldMayBeFinal"})
public class BusPins65816
    implements Pins65816
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
  private boolean memoryLockB;           //output
  private boolean mx;                   //output
  private boolean ready;                //bi-directional.  Also.  Fuck-it I'm treating this an output.
  private boolean resetB;               //input
  private boolean vectorPullB;          //output
  private boolean validProgramAddress;  //output
  private boolean validDataAddress;     //output

  public BusPins65816()
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
    this.memoryLockB = false;
    this.mx = true;
    this.ready = false;
    this.resetB = true;
    this.vectorPullB = true;
    this.validProgramAddress = false;
    this.validDataAddress = false;
  }

  @Override
  public int getAddress()
  {
    return address;
  }

  @Override
  public void setAddress(int address)
  {
    this.address = address;
  }

  @Override
  public int getData()
  {
    return data;
  }

  @Override
  public void setData(int data)
  {
    this.data = data;
  }

  @Override
  public void setRead(boolean read)
  {

    this.read = read;
  }

  @Override
  public boolean isRead()
  {
    return read;
  }

  @Override
  public boolean getPhi2()
  {
    return phi2;
  }

  @Override
  public void setPhi2(boolean phi2)
  {
    this.phi2 = phi2;
  }

  @Override
  public void setEmulation(boolean emulation)
  {
    this.emulation = emulation;
  }

  @Override
  public void setMemoryLockB(boolean memoryLock)
  {
    this.memoryLockB = memoryLock;
  }

  @Override
  public void setMX(boolean mx)
  {
    this.mx = mx;
  }

  @Override
  public void setReady(boolean ready)
  {
    this.ready = ready;
  }

  @Override
  public void setVectorPullB(boolean vectorPullB)
  {
    this.vectorPullB = vectorPullB;
  }

  @Override
  public boolean isValidProgramAddress()
  {
    return validProgramAddress;
  }

  @Override
  public void setValidProgramAddress(boolean validProgramAddress)
  {
    this.validProgramAddress = validProgramAddress;
  }

  @Override
  public boolean isValidDataAddress()
  {
    return validDataAddress;
  }

  @Override
  public void setValidDataAddress(boolean validDataAddress)
  {
    this.validDataAddress = validDataAddress;
  }

  @Override
  public boolean isEmulation()
  {
    return emulation;
  }

  @Override
  public boolean isMemoryLockB()
  {
    return memoryLockB;
  }

  @Override
  public boolean isReady()
  {
    return ready;
  }

  @Override
  public boolean isVectorPullB()
  {
    return vectorPullB;
  }

  public boolean isAbortB()
  {
    return abortB;
  }

  public boolean isBusEnable()
  {
    return busEnable;
  }

  public boolean isPhi2()
  {
    return phi2;
  }

  public boolean isIrqB()
  {
    return irqB;
  }

  public boolean isNmiB()
  {
    return nmiB;
  }

  public boolean isMX()
  {
    return mx;
  }

  public boolean isResetB()
  {
    return resetB;
  }
}

