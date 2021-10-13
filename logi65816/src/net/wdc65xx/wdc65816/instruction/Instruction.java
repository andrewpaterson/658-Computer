package net.wdc65xx.wdc65816.instruction;

import net.wdc65xx.wdc65816.instruction.address.InstructionCycles;

public class Instruction
{
  protected final int code;
  protected final String name;
  protected final AddressingMode addressingMode;
  protected final InstructionCycles cycles;
  protected final String description;

  public Instruction(int code, InstructionCycles cycles, String name, String description)
  {
    this.description = description;
    this.code = code;
    this.name = name;
    this.addressingMode = cycles.getAddressingMode();
    this.cycles = cycles;
  }

  public int getCode()
  {
    return code;
  }

  public String getName()
  {
    return name;
  }

  public InstructionCycles getCycles()
  {
    return cycles;
  }
}

