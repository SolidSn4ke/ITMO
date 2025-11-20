import Papa, { ParseResult } from "papaparse";
import Worker, { FlatWorker } from "../ts/data/Worker";
import { FileUploader } from "react-drag-drop-files";

interface DragNDropProps {
    types: [string];
    onWorkersLoaded?: (workers: Worker[]) => void;
}

function DragNDrop({ types, onWorkersLoaded }: DragNDropProps) {

    const handleChange = (file: any) => {

        Papa.parse<FlatWorker>(file, {
            header: true,
            skipEmptyLines: true,

            complete: (results: ParseResult<FlatWorker>) => {

                const workers: Worker[] = results.data
                    .map((row: FlatWorker) => {
                        // convert plain object -> FlatWorker instance
                        const flat = Object.assign(new FlatWorker(), row);
                        return flat.toWorker();
                    })
                    .filter((w): w is Worker => w !== undefined); // remove invalid rows

            
                console.log("Workers loaded:", workers);

                if (onWorkersLoaded)
                    onWorkersLoaded(workers);
            },

            error: (error) => {
                console.error("CSV parse failed:", error);
            }
        });
    };

    return (
        <FileUploader
            handleChange={handleChange}
            name="file"
            types={types}
        />
    );
}

export default DragNDrop;
