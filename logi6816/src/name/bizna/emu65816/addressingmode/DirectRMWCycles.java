package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Direct;
import static name.bizna.emu65816.Width.A;

public class DirectRMWCycles
    extends InstructionCycles
{
  //10b
  public DirectRMWCycles(Consumer<Cpu65816> operation)
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(RMW), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_DataLow(RMW)),
          new BusCycle(Address(DP(), D0(), o(1)), Read_DataHigh(RMW), new NoteOne(A)),
          new BusCycle(Address(DP(), D0(), o(1)), IO(RMW), E(operation)),
          new BusCycle(Address(DP(), D0(), o(1)), Write_DataHigh(RMW), new NoteOne(A)),
          new BusCycle(Address(DP(), D0()), Write_DataLow(RMW), DONE()));
  }
}
