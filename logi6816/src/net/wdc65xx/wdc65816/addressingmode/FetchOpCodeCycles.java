package net.wdc65xx.wdc65816.addressingmode;

import static net.wdc65xx.wdc65816.AddressingMode.OpCode;

public class FetchOpCodeCycles
    extends InstructionCycles
{
  //22b
  public FetchOpCodeCycles()
  {
    super(OpCode,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()));
  }

  @Override
  protected void validateDoneOperation()
  {
  }
}

