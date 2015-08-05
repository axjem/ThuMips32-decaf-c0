package decaf.machdesc;

import java.io.PrintWriter;
import java.util.List;

import decaf.dataflow.FlowGraph;

public interface MachineDescription {
	
	public void setOutputStream(PrintWriter pw);
	
	public void emitGlobalAlloc(int global_size);

	public void emitAsm(List<FlowGraph> gs);
}
