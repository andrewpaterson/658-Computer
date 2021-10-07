package name.bizna.cpu.addressingmode;

import static name.bizna.cpu.AddressingMode.StackDirectIndirect;

public class StackPEICycles
    extends InstructionCycles
{
  //22e
  public StackPEICycles()
  {
    super(StackDirectIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(S()), Write_AAH(), SP_dec()),
          new BusCycle(Address(S()), Write_AAL(), SP_dec(), DONE()));
  }
}

