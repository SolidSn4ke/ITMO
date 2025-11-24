import Papa, { ParseResult } from "papaparse";
import Worker, { FlatWorker } from "../ts/data/Worker";
import { FileUploader } from "react-drag-drop-files";
import axios from "axios";
import { showErrorNotification, showInfoNotification } from "./Main";

interface DragNDropProps {
    types: [string];
    url: string;
}

function DragNDrop({ types, url }: DragNDropProps) {

    const handleChange = (file: any) => {

        Papa.parse<FlatWorker>(file, {
            header: true,
            skipEmptyLines: true,

            complete: (results: ParseResult<FlatWorker>) => {

                const workers: Worker[] = results.data
                    .map((row: FlatWorker) => {
                        const flat = Object.assign(new FlatWorker(), row);
                        return flat.toWorker()?.toJSON();
                    })
                    .filter((w): w is Worker => w !== undefined);


                console.log("Workers loaded:", workers);

                handleImport(workers);
            },

            error: (error) => {
                console.error("CSV parse failed:", error);
            }
        });
    };

    const handleImport = async (workers: Worker[]) => {
        try {
            let response = await axios.post(url, workers, { withCredentials: true });
            if (response.status === 200) {
                showInfoNotification("Работники успешно импортированы.");
            } else {
                showErrorNotification("Не удалось импортировать файл")
            }
        } catch (error) {
            console.log(error);
        }
    }

    return (
        <FileUploader
            handleChange={handleChange}
            name="file"
            types={types}
        />
    );
}

export default DragNDrop;
