/* ***** BEGIN LICENSE BLOCK *****
 *
 * Copyright (c) 2014-2019 Bruno Augusto Nassif Travencolo.
 * All Rights Reserved.
 *
 * This file is part of DyNetVis Project (DyNetVis).
 *
 * How to cite this software:
 *  
@inproceedings{Linhares:2017:DSV:3019612.3019686,
 author = {Linhares, Claudio D. G. and Traven\c{c}olo, Bruno A. N. and Paiva, Jose Gustavo S. and Rocha, Luis E. C.},
 title = {DyNetVis: A System for Visualization of Dynamic Networks},
 booktitle = {Proceedings of the Symposium on Applied Computing},
 series = {SAC '17},
 year = {2017},
 isbn = {978-1-4503-4486-9},
 location = {Marrakech, Morocco},
 pages = {187--194},
 numpages = {8},
 url = {http://doi.acm.org/10.1145/3019612.3019686},
 doi = {10.1145/3019612.3019686},
 acmid = {3019686},
 publisher = {ACM},
 address = {New York, NY, USA},
 keywords = {complex networks, dynamic graph visualization, dynamic networks, recurrent neighbors, temporal activity map},
} 
 *  
 * DyNetVis is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version.
 *
 * DyNetVis is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * This code was developed by members of Lab of Complex Network Visualization at 
 * Federal University of Uberlândia, Brazil - (https://sites.google.com/view/dynetvis/team?authuser=0). 
 * The initial developer of the original code is Claudio D. G. Linhares <claudiodgl@gmail.com>.
 *
 * Contributor(s): Jean R. Ponciano -- jeanrobertop@gmail.com, Luis E. C. Rocha -- luis.rocha@ugent.be, 
 * José Gustavo S. Paiva -- gustavo@ufu.br, Bruno A. N. Travençolo -- travencolo@gmail.com
 *
 * You should have received a copy of the GNU General Public License along 
 * with DyNetVis. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */

package layout;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.model.mxCell;
import com.mxgraph.shape.mxConnectorShape;
import com.mxgraph.util.mxLine;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxCellState;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.List;

/**
 *
 * @author Claudio Linhares
 */

class CurvedShape extends mxConnectorShape {

    public static final String KEY = "curvedEdge";
    private GeneralPath path;

    @Override
    public void paintShape(mxGraphics2DCanvas canvas, mxCellState state) {

        List<mxPoint> abs = state.getAbsolutePoints();
        int n = state.getAbsolutePointCount();
        mxCell aux = (mxCell)state.getCell();
        if (n < 3) {
            super.paintShape(canvas, state);
        } else if (configureGraphics(canvas, state, false)) {
            Graphics2D g = canvas.getGraphics();
            path = createPath(abs);
            g.draw(path);
            paintMarker(canvas, state, false);
            paintMarker(canvas, state, true);
        }
    }

    /* Code borrowed from here: http://www.codeproject.com/Articles/31859/Draw-a-Smooth-Curve-through-a-Set-of-2D-Points-wit */
    public static GeneralPath createPath(List<mxPoint> abs) {
        mxPoint[] knots = abs.toArray(new mxPoint[abs.size()]);

        int n = knots.length - 1;
        mxPoint[] firstControlPoints = new mxPoint[n];
        mxPoint[] secondControlPoints = new mxPoint[n];    // Calculate first Bezier control points    // Right hand side vector    
        double[] rhs = new double[n];    // Set right hand side X values    
        for (int i = 1; i < n - 1; ++i) {
            rhs[i] = 4 * knots[i].getX() + 2 * knots[i + 1].getX();
        }
        rhs[0] = knots[0].getX() + 2 * knots[1].getX();
        rhs[n - 1] = (8 * knots[n - 1].getX() + knots[n].getX()) / 2.0;    // Get first control points X-values    
        double[] x = getFirstControlPoints(rhs);    // Set right hand side Y values    
        for (int i = 1; i < n - 1; ++i) {
            rhs[i] = 4 * knots[i].getY() + 2 * knots[i + 1].getY();
        }
        rhs[0] = knots[0].getY() + 2 * knots[1].getY();
        rhs[n - 1] = (8 * knots[n - 1].getY() + knots[n].getY()) / 2.0;    // Get first control points Y-values    
        double[] y = getFirstControlPoints(rhs);    // Fill output arrays.    
        for (int i = 0; i < n; ++i) {        // First control point        
            firstControlPoints[i] = new mxPoint(x[i], y[i]);        // Second control point        
            if (i < n - 1) {
                secondControlPoints[i] = new mxPoint(2 * knots[i + 1].getX() - x[i + 1], 2 * knots[i + 1].getY() - y[i + 1]);
            } else {
                secondControlPoints[i] = new mxPoint((knots[n].getX() + x[n - 1]) / 2, (knots[n].getY() + y[n - 1]) / 2);
            }
        }
        GeneralPath path = new GeneralPath();
        path.moveTo(knots[0].getX(), knots[0].getY());
        for (int i = 1; i < n + 1; i++) {
            path.curveTo(firstControlPoints[i - 1].getX(), firstControlPoints[i - 1].getY(), secondControlPoints[i - 1].getX(), secondControlPoints[i - 1].getY(), knots[i].getX(), knots[i].getY());
        }
        return path;
    }/// <summary>/// Solves a tridiagonal system for one of coordinates (x or y)/// of first Bezier control points./// </summary>/// <param name="rhs">Right hand side vector.</param>/// <returns>Solution vector.</returns>

    private static double[] getFirstControlPoints(double[] rhs) {
        int n = rhs.length;
        double[] x = new double[n]; // Solution vector.    
        double[] tmp = new double[n]; // Temp workspace.    
        double b = 2.0;
        x[0] = rhs[0] / b;
        for (int i = 1; i < n; i++) // Decomposition and forward substitution.    
        {
            tmp[i] = 1 / b;
            b = (i < n - 1 ? 4.0 : 3.5) - tmp[i];
            x[i] = (rhs[i] - x[i - 1]) / b;
        }
        for (int i = 1; i < n; i++) {
            x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.    
        }
        return x;
    }

    @Override
    protected mxLine getMarkerVector(List<mxPoint> points, boolean source, double markerSize) {
        if (path == null || points.size() < 3) {
            return super.getMarkerVector(points, source, markerSize);
        }
        double coords[] = new double[6];
        double x0 = 0;
        double y0 = 0;
        double x1 = 0;
        double y1 = 0;
        PathIterator p = path.getPathIterator(null, 2.0);
        if (source) {
            p.currentSegment(coords);
            x1 = coords[0];
            y1 = coords[1];
            p.next();
            p.currentSegment(coords);
            x0 = coords[0];
            y0 = coords[1];
        } else {
            while (!p.isDone()) {
                p.currentSegment(coords);
                x0 = x1;
                y0 = y1;
                x1 = coords[0];
                y1 = coords[1];
                p.next();
            }
        }
        return new mxLine(x0, y0, new mxPoint(x1, y1));
    }


}