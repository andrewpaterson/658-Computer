package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import static name.bizna.cpu.AddressingMode.Absolute;
import static name.bizna.cpu.WidthFromRegister.M;

public class AbsoluteRMWCycles
    extends InstructionCycles
{
  //1d
  public AbsoluteRMWCycles(Executor<Cpu65816> operation)
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(RMW), PC_inc()),
          new BusCycle(Address(DBR(), AA()), Read_DataLow(RMW)),
          new BusCycle(Address(DBR(), AA(), o(1)), Read_DataHigh(RMW), new NoteOne(M)),
          new BusCycle(Address(DBR(), AA(), o(1)), IO(RMW), E(operation)),
          new BusCycle(Address(DBR(), AA(), o(1)), Write_DataHigh(RMW), new NoteOne(M)),
          new BusCycle(Address(DBR(), AA()), Write_DataLow(RMW), DONE()));
  }
}

