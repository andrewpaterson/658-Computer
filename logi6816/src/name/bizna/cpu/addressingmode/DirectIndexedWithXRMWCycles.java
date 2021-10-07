package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.DirectIndexedWithX;
import static name.bizna.cpu.Width.A;

public class DirectIndexedWithXRMWCycles
    extends InstructionCycles
{
  //16b
  public DirectIndexedWithXRMWCycles(Consumer<Cpu65816> operation)
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(RMW), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO(RMW)),
          new BusCycle(Address(DP(), D0(), X()), Read_DataLow(RMW)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_DataHigh(RMW), new NoteOne(A)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), IO(RMW), E(operation)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Write_DataHigh(RMW), new NoteOne(A)),
          new BusCycle(Address(DP(), D0(), X()), Write_DataLow(RMW), DONE()));
  }
}

