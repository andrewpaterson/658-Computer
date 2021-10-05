package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.EmulatorException;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode
{
  protected final int code;
  protected final String name;
  protected final AddressingMode addressingMode;
  protected final InstructionCycles cycles;
  protected final String description;

  public OpCode(String name, String description, int code, InstructionCycles cycles)
  {
    this.description = description;
    this.code = code;
    this.name = name;
    this.addressingMode = cycles.getAddressingMode();
    this.cycles = cycles;
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

  public InstructionCycles getCycles()
  {
    return cycles;
  }
}

