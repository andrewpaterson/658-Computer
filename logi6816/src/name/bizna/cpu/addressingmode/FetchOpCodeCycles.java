package name.bizna.cpu.addressingmode;

import static name.bizna.cpu.AddressingMode.OpCode;

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

