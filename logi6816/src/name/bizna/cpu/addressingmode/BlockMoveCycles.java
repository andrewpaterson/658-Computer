package name.bizna.cpu.addressingmode;

import name.bizna.cpu.AddressingMode;
import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import java.util.function.Consumer;

public class BlockMoveCycles
    extends InstructionCycles
{
  //9a & 9b
  public BlockMoveCycles(Executor<Cpu65816> operation)
  {
    super(AddressingMode.BlockMove,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_DBR(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), X()), Read_DataLow()),
          new BusCycle(Address(DBR(), Y()), Write_DataLow(), PC_dec(), E(operation)),
          new BusCycle(Address(DBR(), Y()), IO(), PC_dec()),
          new BusCycle(Address(DBR(), Y()), IO(), PC_dec(), DONE()));
  }
}

