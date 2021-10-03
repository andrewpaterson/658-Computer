package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelative;

public class StackRelativeCycles
    extends InstructionCycles
{
  public StackRelativeCycles(boolean read)
  {
    super(StackRelative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), D0()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(S(), D0(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

