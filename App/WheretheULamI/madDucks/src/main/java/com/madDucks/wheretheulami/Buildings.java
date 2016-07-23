package com.madDucks.wheretheulami;

/**
 * Created by Ross on 24/03/14.
 */

public class Buildings {

    Point2D Kemmy = new Point2D(52.672609,-8.577168);
    Point2D CSIS = new Point2D(52.673887,-8.575575);
    Point2D Library = new Point2D(52.673311,-8.57352);
    Point2D A = new Point2D(52.673887,-8.570028);
    Point2D B = new Point2D( 52.673581,-8.570768);
    Point2D C = new Point2D( 52.673571,-8.572088);
    Point2D D = new Point2D( 52.673994,-8.571863);
    Point2D E = new Point2D( 52.674336,-8.572018);
    Point2D Arena = new Point2D( 52.67362,-8.565211);
    Point2D Schrodinger = new Point2D( 52.673812,-8.567394);
    Point2D Foundation = new Point2D( 52.674358,-8.57352);
    Point2D StudentsUnion = new Point2D( 52.673174,-8.570307);
    Point2D Engineering = new Point2D( 52.675458,-8.573391);
    Point2D Languages = new Point2D( 52.675614,-8.573472);
    Point2D Schuman = new Point2D( 52.673103,-8.577887);
    Point2D Tierney = new Point2D (52.674489,-8.577152);
    Point2D Lonsdale = new Point2D( 52.673854,-8.568856);
    Point2D PES = new Point2D( 52.67469,-8.567869);
    Point2D HealthScience = new Point2D( 52.677634,-8.568915);

    public Buildings()
    {
    }

    public double distance2building(Point2D loc, String building)
    {
        if(building.equals("Kemmy Building"))
        {
            return loc.distance(Kemmy);
        }

        if(building.equals("CSIS Building"))
        {
            return loc.distance(CSIS);
        }

        if(building.equals("Library Building"))
        {
            return loc.distance(Library);
        }

        if(building.equals("Block A"))
        {
            return loc.distance(A);
        }

        if(building.equals("Block B"))
        {
            return loc.distance(B);
        }

        if(building.equals("Block C"))
        {
            return loc.distance(C);
        }

        if(building.equals("Block D"))
        {
            return loc.distance(D);
        }

        if(building.equals("Block E"))
        {
            return loc.distance(E);
        }

        if(building.equals("Sports Arena"))
        {
            return loc.distance(Arena);
        }

        if(building.equals("Schrodinger Building"))
        {
            return loc.distance(Schrodinger);
        }

        if(building.equals("Foundation Building"))
        {
            return loc.distance(Foundation);
        }

        if(building.equals("Students Union"))
        {
            return loc.distance(StudentsUnion);
        }

        if(building.equals("Engineering Building"))
        {
            return loc.distance(Engineering);
        }

        if(building.equals("Languages Building"))
        {
            return loc.distance(Languages);
        }

        if(building.equals("Schuman Building"))
        {
            return loc.distance(Schuman);
        }

        if(building.equals("Tierney Building"))
        {
            return loc.distance(Tierney);
        }

        if(building.equals("Lonsdale Building"))
        {
            return loc.distance(Lonsdale);
        }

        if(building.equals("PES Building"))
        {
            return loc.distance(PES);
        }

        if(building.equals("Health Science Building"))
        {
            return loc.distance(HealthScience);
        }

        return 0;
    }

    public Point2D coordinatesOfBuilding(String building)
    {
        if(building.equals("Kemmy Building"))
        {
            return (Kemmy);
        }

        if(building.equals("CSIS Building"))
        {
            return (CSIS);
        }

        if(building.equals("Library Building"))
        {
            return (Library);
        }

        if(building.equals("Block A"))
        {
            return (A);
        }

        if(building.equals("Block B"))
        {
            return (B);
        }

        if(building.equals("Block C"))
        {
            return (C);
        }

        if(building.equals("Block D"))
        {
            return (D);
        }

        if(building.equals("Block E"))
        {
            return (E);
        }

        if(building.equals("Sports Arena"))
        {
            return (Arena);
        }

        if(building.equals("Schrodinger Building"))
        {
            return (Schrodinger);
        }

        if(building.equals("Foundation Building"))
        {
            return (Foundation);
        }

        if(building.equals("Students Union"))
        {
            return (StudentsUnion);
        }

        if(building.equals("Engineering Building"))
        {
            return (Engineering);
        }

        if(building.equals("Languages Building"))
        {
            return (Languages);
        }

        if(building.equals("Schuman Building"))
        {
            return (Schuman);
        }

        if(building.equals("Tierney Building"))
        {
            return (Tierney);
        }

        if(building.equals("Lonsdale Building"))
        {
            return (Lonsdale);
        }

        if(building.equals("PES Building"))
        {
            return (PES);
        }

        if(building.equals("Health Science Building"))
        {
            return (HealthScience);
        }

        return null;
    }

}
