package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.wdc65xx.wdc65816.Executor;

import static net.wdc65xx.wdc65816.AddressingMode.Direct;
import static net.wdc65xx.wdc65816.WidthFromRegister.M;

public class DirectRMWCycles
    extends InstructionCycles
{
  //10b
  public DirectRMWCycles(Executor<Cpu65816> operation)
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_DataLow(RMW)),
          new BusCycle(Address(DP(), D0(), o(1)), Read_DataHigh(RMW), new NoteOne(M)),
          new BusCycle(Address(DP(), D0(), o(1)), IO(RMW), E(operation)),
          new BusCycle(Address(DP(), D0(), o(1)), Write_DataHigh(RMW), new NoteOne(M)),
          new BusCycle(Address(DP(), D0()), Write_DataLow(RMW), DONE()));
  }
}

