package name.bizna.emu65816;

@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "unused"})
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
  private boolean memoryLockB;           //output
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
    this.memoryLockB = false;
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

  public boolean getPhi2()
  {
    return phi2;
  }

  public void setPhi2(boolean phi2)
  {
    this.phi2 = phi2;
  }

  public void setEmulation(boolean emulation)
  {
    this.emulation = emulation;
  }

  public void setMemoryLockB(boolean memoryLock)
  {
    this.memoryLockB = memoryLock;
  }

  public void setMX(boolean mx)
  {
    this.mx = mx;
  }

  public void setReady(boolean ready)
  {
    this.ready = ready;
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

