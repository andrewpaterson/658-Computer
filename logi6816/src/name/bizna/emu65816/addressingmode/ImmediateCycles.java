package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImmediateCycles
    extends InstructionCycles
{
  //18
  public ImmediateCycles()
  {
    //The instruction must work out when to go to the next instruction on execute.  REP and SEP are an 8bit problem.
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_IDL(), new Execute1()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_IDH(), new Execute2(), new NoteOne(), new NoteEight()));
  }

  private static FetchImmediateDataHigh Read_IDH()
  {
    return new FetchImmediateDataHigh();
  }

  private static FetchImmediateDataLow Read_IDL()
  {
    return new FetchImmediateDataLow();
  }
}

