package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;

public abstract class OpCode
{
  private final int code;
  private final String name;
  private final AddressingMode addressingMode;

  public OpCode(String name, int code, AddressingMode addressingMode)
  {
    this.code = code;
    this.name = name;
    this.addressingMode = addressingMode;
  }

  protected void invalidCycle()
  {
    throw new EmulatorException("Invalid Cycle");
  }

  public int getCode()
  {
    return code;
  }

  public String getName()
  {
    return name;
  }

  public AddressingMode getAddressingMode()
  {
    return addressingMode;
  }

  public abstract void executeOnFallingEdge(Cpu65816 cpu);

  public abstract void executeOnRisingEdge(Cpu65816 cpu);
}

