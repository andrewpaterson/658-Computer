package net.logicim.ui;

import net.logicim.common.SimulatorException;
import net.logicim.common.geometry.Line;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Circuit;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.trace.TraceNet;
import net.logicim.ui.common.*;
import net.logicim.ui.shape.common.BoundingBox;

import java.awt.*;
import java.util.List;
import java.util.*;

public class CircuitEditor
{
  protected List<DiscreteView> discreteViews;
  protected List<TraceView> traceViews;
  protected Circuit circuit;
  protected Simulation simulation;

  public CircuitEditor()
  {
    circuit = new Circuit();
    discreteViews = new ArrayList<>();
    traceViews = new ArrayList<>();
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    for (TraceView traceView : traceViews)
    {
      traceView.paint(graphics, viewport);
    }

    for (DiscreteView discreteView : discreteViews)
    {
      discreteView.paint(graphics, viewport);
    }
  }

  public Circuit getCircuit()
  {
    return circuit;
  }

  public void add(DiscreteView view)
  {
    discreteViews.add(view);
  }

  public void add(TraceView view)
  {
    traceViews.add(view);
  }

  public void remove(IntegratedCircuitView<?> integratedCircuitView)
  {
    List<PortView> portViews = integratedCircuitView.getPorts();
    for (PortView portView : portViews)
    {
      ConnectionView connection = portView.getConnection();
      disconnectTraceNet(findConnections(connection));
    }

    IntegratedCircuit<?, ?> integratedCircuit = integratedCircuitView.getIntegratedCircuit();
    circuit.remove(integratedCircuit);
    discreteViews.remove(integratedCircuitView);
  }

  public void remove(TraceView traceView)
  {
    ConnectionView startConnection = traceView.getStartConnection();
    ConnectionView endConnection = traceView.getEndConnection();

    disconnectTraceNet(findConnections(startConnection));
    disconnectTraceNet(findConnections(endConnection));

    startConnection.remove(traceView);
    endConnection.remove(traceView);
    traceView.removed();
    traceViews.remove(traceView);
  }

  public void disconnectTraceNet(Set<ConnectionView> connectionsNet)
  {
    for (ConnectionView connection : connectionsNet)
    {
      List<ComponentView> connectedComponents = connection.getConnectedComponents();
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          ((TraceView) connectedComponent).disconnectTraceNet();
        }
        else if (connectedComponent instanceof IntegratedCircuitView)
        {
          Int2D position = connection.getGridPosition();
          if (position != null)
          {
            PortView portView = ((IntegratedCircuitView<?>) connectedComponent).getPortInGrid(position);
            portView.disconnectTraceNet();
          }
        }
      }
    }
  }

  public Simulation reset()
  {
    return circuit.resetSimulation();
  }

  public void runSimultaneous()
  {
    ensureSimulation();

    simulation.runSimultaneous();
  }

  public void runToTime(long timeForward)
  {
    ensureSimulation();

    simulation.runToTime(timeForward);
  }

  public void ensureSimulation()
  {
    if (simulation == null)
    {
      simulation = circuit.resetSimulation();
    }
  }

  public DiscreteView getDiscreteViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<DiscreteView> selectedViews = getDiscreteViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D boundBoxPosition = new Int2D();
      Int2D boundBoxDimension = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      DiscreteView closestView = null;
      for (DiscreteView view : selectedViews)
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        boundBoxPosition.add(boundBoxDimension.x / 2, boundBoxDimension.y / 2);
        float distance = BoundingBox.calculateDistance(screenPosition, boundBoxPosition);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<DiscreteView> getDiscreteViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    Int2D boundBoxPosition = new Int2D();
    Int2D boundBoxDimension = new Int2D();
    List<DiscreteView> selectedViews = new ArrayList<>();
    for (DiscreteView view : discreteViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInScreenSpace(viewport, boundBoxPosition, boundBoxDimension);
        if (BoundingBox.isContained(screenPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }

  public List<DiscreteView> getDiscreteViewsInGridSpace(Int2D gridPosition)
  {
    Float2D boundBoxPosition = new Float2D();
    Float2D boundBoxDimension = new Float2D();
    List<DiscreteView> selectedViews = new ArrayList<>();
    for (DiscreteView view : discreteViews)
    {
      if (view.isEnabled())
      {
        view.getBoundingBoxInGridSpace(boundBoxPosition, boundBoxDimension);
        if (BoundingBox.isContained(gridPosition, boundBoxPosition, boundBoxDimension))
        {
          selectedViews.add(view);
        }
      }
    }
    return selectedViews;
  }

  public List<TraceView> getTraceViewsInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    int x = viewport.transformScreenToGridX(screenPosition.x);
    int y = viewport.transformScreenToGridY(screenPosition.y);
    List<TraceView> traceViews = new ArrayList<>();
    for (TraceView traceView : this.traceViews)
    {
      if (traceView.contains(x, y))
      {
        traceViews.add(traceView);
      }
    }
    return traceViews;
  }

  public TraceView getTraceViewInScreenSpace(Viewport viewport, Int2D screenPosition)
  {
    List<TraceView> selectedViews = getTraceViewsInScreenSpace(viewport, screenPosition);

    if (selectedViews.size() == 1)
    {
      return selectedViews.get(0);
    }
    else if (selectedViews.size() == 0)
    {
      return null;
    }
    else
    {
      Int2D center = new Int2D();
      float shortestDistance = Float.MAX_VALUE;
      TraceView closestView = null;
      for (TraceView view : selectedViews)
      {
        view.getCenter(center);
        float distance = BoundingBox.calculateDistance(screenPosition, center);
        if (distance < shortestDistance)
        {
          closestView = view;
          shortestDistance = distance;
        }
      }
      return closestView;
    }
  }

  public List<TraceView> getTraceViewsInGridSpace(Int2D position)
  {
    List<TraceView> result = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      if (traceView.contains(position))
      {
        result.add(traceView);
      }
    }
    return result;
  }

  public List<TraceOverlap> getTracesOverlapping(Line line)
  {
    List<TraceOverlap> overlaps = new ArrayList<>();
    for (TraceView traceView : traceViews)
    {
      TraceOverlap overlap = traceView.getOverlap(line);
      if (overlap != null)
      {
        overlaps.add(overlap);
      }
    }
    return overlaps;
  }

  public List<ComponentView> getComponents(Int2D position)
  {
    ConnectionView connectionView = getConnection(position);
    return connectionView.getConnectedComponents();
  }

  public ConnectionView getConnection(Int2D position)
  {
    List<DiscreteView> discreteViews = getDiscreteViewsInGridSpace(position);
    List<TraceView> traceViews = getTraceViewsInGridSpace(position);
    Set<ConnectionView> connectionViews = new HashSet<>();

    for (TraceView traceView : traceViews)
    {
      ConnectionView connectionView = traceView.getConnectionsInGrid(position);
      if (connectionView != null)
      {
        connectionViews.add(connectionView);
      }
    }

    for (DiscreteView discreteView : discreteViews)
    {
      PortView portView = discreteView.getPortInGrid(position.x, position.y);
      if (portView != null)
      {
        connectionViews.add(portView.getConnection());
      }
    }

    if (connectionViews.size() == 1)
    {
      return connectionViews.iterator().next();
    }
    else if (connectionViews.size() == 0)
    {
      return null;
    }
    else
    {
      throw new SimulatorException("Expected all connections at location [" + position.toString() + "] to be the same.");
    }
  }

  public ConnectionView getOrAddConnection(Int2D position, ComponentView componentView)
  {
    ConnectionView connection = getConnection(position);
    if (connection != null)
    {
      connection.add(componentView);
      return connection;
    }
    else
    {
      return new ConnectionView(componentView);
    }
  }

  public Set<ConnectionView> findConnections(ConnectionView connection)
  {
    if (connection != null)
    {
      ArrayList<ConnectionView> connectionsToProcess = new ArrayList<>();
      connectionsToProcess.add(connection);
      return findConnections(connectionsToProcess);
    }
    else
    {
      return new LinkedHashSet<>();
    }
  }

  public Set<ConnectionView> findConnections(List<ConnectionView> connectionsToProcess)
  {
    Set<ConnectionView> connectionsNet = new LinkedHashSet<>();
    while (connectionsToProcess.size() > 0)
    {
      ConnectionView currentConnection = connectionsToProcess.get(0);
      connectionsToProcess.remove(0);
      connectionsNet.add(currentConnection);

      List<ComponentView> components = currentConnection.getConnectedComponents();
      if (components != null)
      {
        for (ComponentView component : components)
        {
          if (component instanceof TraceView)
          {
            TraceView traceView = (TraceView) component;
            ConnectionView opposite = traceView.getOpposite(currentConnection);
            if (opposite != null)
            {
              if (!connectionsNet.contains(opposite))
              {
                connectionsToProcess.add(opposite);
              }
            }
          }
        }
      }
    }
    return connectionsNet;
  }

  public void connectToTraceNet(Set<ConnectionView> connectionsNet, TraceNet trace)
  {
    for (ConnectionView connection : connectionsNet)
    {
      List<ComponentView> connectedComponents = connection.getConnectedComponents();
      for (ComponentView connectedComponent : connectedComponents)
      {
        if (connectedComponent instanceof TraceView)
        {
          ((TraceView) connectedComponent).connect(trace);
        }
        else if (connectedComponent instanceof IntegratedCircuitView)
        {
          Int2D position = connection.getGridPosition();
          if (position != null)
          {
            PortView portView = ((IntegratedCircuitView<?>) connectedComponent).getPortInGrid(position);
            portView.connectTraceNet(trace);
          }
        }
      }
    }
  }
}

